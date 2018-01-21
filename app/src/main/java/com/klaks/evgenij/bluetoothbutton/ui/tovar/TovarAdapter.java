package com.klaks.evgenij.bluetoothbutton.ui.tovar;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.klaks.evgenij.bluetoothbutton.Common;
import com.klaks.evgenij.bluetoothbutton.R;
import com.klaks.evgenij.bluetoothbutton.model.Tovar;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class TovarAdapter extends RecyclerView.Adapter<TovarAdapter.Holder> {

    private List<Tovar> tovar;

    public TovarAdapter(List<Tovar> tovar) {
        this.tovar = tovar;
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView image;
        TextView price;
        ImageButton remove;
        TextView count;
        ImageButton add;
        TextView description;

        public Holder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            price = itemView.findViewById(R.id.price);
            remove = itemView.findViewById(R.id.remove);
            count = itemView.findViewById(R.id.count);
            add = itemView.findViewById(R.id.add);
            description = itemView.findViewById(R.id.description);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tovar, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        Tovar tovar = this.tovar.get(position);
        holder.name.setText(tovar.getName());
        holder.price.setText(String.format(Locale.getDefault(),"%1.2f р.", tovar.getPrice()));
        holder.count.setText(String.format(Locale.getDefault(), "%1d", tovar.getCount()));
        holder.description.setText(getHtml(tovar.getDescription()));
        Picasso.with(holder.name.getContext())
                .load(Common.BASE_URL + tovar.getImage())
                .into(holder.image);

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRemoveCount(holder, true);
            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRemoveCount(holder, false);
            }
        });
    }

    private Spanned getHtml(String text) {
        if (Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(text);
        }
    }


    private void addRemoveCount(Holder holder, boolean add) {
        Tovar tovar = this.tovar.get(holder.getAdapterPosition());
        if (add) {
            tovar.setCount(tovar.getCount() + 1);
        } else {
            if (tovar.getCount() != 0)
                tovar.setCount(tovar.getCount() - 1);
        }
        holder.count.setText(String.format(Locale.getDefault(), "%1d", tovar.getCount()));
    }

    @Override
    public int getItemCount() {
        return tovar.size();
    }
}
