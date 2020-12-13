package by.bstu.razvod.lab4.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import by.bstu.razvod.lab4.MainViewPresentation;
import by.bstu.razvod.lab4.R;
import by.bstu.razvod.lab4.extendes.ClickListener;
import by.bstu.razvod.lab4.extendes.ContextMenuListener;
import by.bstu.razvod.lab4.extendes.LongClickListener;

public class MainAdapter extends RecyclerView.Adapter<MainItemAdapter> {

    private LayoutInflater inflater;
    private List<MainViewPresentation> item;
    private boolean isAnySelected = false;
    private LongClickListener longClickListener;
    private ClickListener clickListener;
    private ContextMenuListener contextMenuListener;

    public MainAdapter(Context context, List<MainViewPresentation> item, @NonNull LongClickListener longClickListener, @NonNull ClickListener clickListener, @NonNull ContextMenuListener contextMenuListener) {
        this.item = item;
        this.inflater = LayoutInflater.from(context);
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
        this.contextMenuListener = contextMenuListener;
    }

    @Override
    public MainItemAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_main, parent, false);
        return new MainItemAdapter(view);
    }

    @Override
    public void onBindViewHolder(MainItemAdapter holder, int position) {
        MainViewPresentation currentItem = (MainViewPresentation) item.get(position);
        holder.bind(currentItem, isAnySelected, longClickListener, clickListener, contextMenuListener);
    }

    public void setItems(List<MainViewPresentation> m) {
        this.item = m;
        isAnySelected = false;
        item.stream().forEach(item -> {
            if (item.isSelected()) {
                isAnySelected = true;
                return;
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

}