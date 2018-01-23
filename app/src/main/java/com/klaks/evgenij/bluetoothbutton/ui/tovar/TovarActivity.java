package com.klaks.evgenij.bluetoothbutton.ui.tovar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klaks.evgenij.bluetoothbutton.Common;
import com.klaks.evgenij.bluetoothbutton.QueryPreferences;
import com.klaks.evgenij.bluetoothbutton.R;
import com.klaks.evgenij.bluetoothbutton.model.ResponseBody;
import com.klaks.evgenij.bluetoothbutton.model.Result;
import com.klaks.evgenij.bluetoothbutton.model.Tovar;
import com.klaks.evgenij.bluetoothbutton.network.ApiFactory;
import com.klaks.evgenij.bluetoothbutton.ui.BaseActivity;
import com.klaks.evgenij.bluetoothbutton.util.HelpTransformer;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class TovarActivity extends BaseActivity implements TovarAdapter.TovarAdapterListener {

    private View progressBar;
    private View contentContainer;
    private View successView;
    private TextView supplier;
    private TextView productGroup;
    private RecyclerView recyclerView;
    private TovarAdapter tovarAdapter;

    private TextView totalCountView;
    private TextView totalPriceView;

    private int totalCount = 0;

    private ResponseBody responseBody;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressBar = findViewById(R.id.progressBar);
        successView = findViewById(R.id.successView);
        contentContainer = findViewById(R.id.contentContainer);
        supplier = findViewById(R.id.suppplier);
        productGroup = findViewById(R.id.product_group);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);

        totalCountView = findViewById(R.id.totalCount);
        totalPriceView = findViewById(R.id.totalPrice);

        String button = getIntent().getStringExtra("button");
        loadTovar(button);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOrder();
            }
        });
    }

    private void loadTovar(String button) {
        ApiFactory.getService().infoButton(button)
                .compose(new HelpTransformer<ResponseBody>())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        TovarActivity.this.responseBody = responseBody;
                        supplier.setText(responseBody.getOrganization().getName());
                        productGroup.setText(responseBody.getButton().getName());
                        tovarAdapter = new TovarAdapter(responseBody.getTovars(), TovarActivity.this);
                        recyclerView.setAdapter(tovarAdapter);
                        progressBar.setVisibility(View.GONE);
                        contentContainer.setVisibility(View.VISIBLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Crashlytics.logException(throwable);
                        throwable.printStackTrace();
                        Toast.makeText(TovarActivity.this, R.string.error_noname, Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCountChanged(int count, double price) {
        totalCount = count;
        totalCountView.setText(Tovar.getFormatCount(count));
        totalPriceView.setText(Tovar.getFormatPrice(price));
    }

    private void sendOrder() {
        if (totalCount > 0) {
            JsonObject mainObject = new JsonObject();
            mainObject.addProperty("button_id", responseBody.getButton().getId());
            mainObject.addProperty("token", QueryPreferences.getToken(this));

            JsonArray array = new JsonArray();
            for (Tovar tovar : responseBody.getTovars()) {
                JsonObject tovarObject = new JsonObject();
                tovarObject.addProperty("id", tovar.getId());
                tovarObject.addProperty("count", tovar.getCount());
                array.add(tovarObject);
            }
            mainObject.add("tovar", array);
            if (checkNetworkState()) {
                ApiFactory.getService()
                        .sendOrder(mainObject.toString())
                        .map(new Function<Result, Result>() {
                            @Override
                            public Result apply(Result result) throws Exception {
                                if (result.isError())
                                    throw new Exception();
                                return result;
                            }
                        })
                        .compose(new HelpTransformer<Result>())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showProgress();
                            }
                        })
                        .subscribe(
                                new Consumer<Result>() {
                                    @Override
                                    public void accept(Result result) throws Exception {
                                        onCountChanged(0, 0.0);
                                        showSuccess();
                                    }
                                },
                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Crashlytics.logException(throwable);
                                        Log.d(Common.TAG, "ОШИБКА "  + throwable.getMessage());
                                        showContent();
                                        Toast.makeText(TovarActivity.this, R.string.error_noname, Toast.LENGTH_LONG).show();
                                    }
                                });

            }
        } else {
            Toast.makeText(this, R.string.select_tovar_for_send_order, Toast.LENGTH_LONG).show();
        }

    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        contentContainer.setVisibility(View.GONE);
        successView.setVisibility(View.GONE);
    }

    private void showContent() {
        progressBar.setVisibility(View.GONE);
        contentContainer.setVisibility(View.VISIBLE);
        successView.setVisibility(View.GONE);
    }

    private void showSuccess() {
        progressBar.setVisibility(View.GONE);
        successView.setVisibility(View.VISIBLE);
        contentContainer.setVisibility(View.GONE);
    }


}
