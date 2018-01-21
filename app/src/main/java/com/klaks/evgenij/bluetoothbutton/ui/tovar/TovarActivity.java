package com.klaks.evgenij.bluetoothbutton.ui.tovar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.klaks.evgenij.bluetoothbutton.R;
import com.klaks.evgenij.bluetoothbutton.model.ResponseBody;
import com.klaks.evgenij.bluetoothbutton.network.ApiFactory;
import com.klaks.evgenij.bluetoothbutton.util.HelpTransformer;

import io.reactivex.functions.Consumer;

public class TovarActivity extends AppCompatActivity {

    private View progressBar;
    private View contentContainer;
    private TextView supplier;
    private TextView productGroup;
    private RecyclerView recyclerView;
    private TovarAdapter tovarAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBar);
        contentContainer = findViewById(R.id.contentContainer);
        supplier = findViewById(R.id.suppplier);
        productGroup = findViewById(R.id.product_group);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);

        String button = getIntent().getStringExtra("button");
        loadTovar(button);


    }

    private void loadTovar(String button) {
        ApiFactory.getService().infoButton(button)
                .compose(new HelpTransformer<ResponseBody>())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        supplier.setText(responseBody.getOrganization().getName());
                        productGroup.setText(responseBody.getButton().getName());
                        tovarAdapter = new TovarAdapter(responseBody.getTovars());
                        recyclerView.setAdapter(tovarAdapter);
                        progressBar.setVisibility(View.GONE);
                        contentContainer.setVisibility(View.VISIBLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*{
        "button_id": 4,
        "token": "dfdfdf24",
        "tovar": [
            {
                "id": 3,
                "count": 18
            },
            {
                "id": 3,
                "count": 0
            }
        ]
    }*/
}
