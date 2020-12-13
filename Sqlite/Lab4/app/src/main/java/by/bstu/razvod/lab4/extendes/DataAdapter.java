package by.bstu.razvod.lab4.extendes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import by.bstu.razvod.lab4.MainViewPresentation;
import by.bstu.razvod.lab4.R;
import by.bstu.razvod.lab4.model.ContactModel;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<MainViewPresentation> item;
    private boolean isAnySelected = false;
    private LongClickListener longClickListener;
    private ClickListener clickListener;
    private ContextMenuListener contextMenuListener;
    private ContactModel contactModel;

    public DataAdapter(Context context, List<MainViewPresentation> item, @NonNull LongClickListener longClickListener, @NonNull ClickListener clickListener, @NonNull ContextMenuListener contextMenuListener) {
        this.item = item;
        this.inflater = LayoutInflater.from(context);
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
        this.contextMenuListener = contextMenuListener;
    }
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        MainViewPresentation currentItem = (MainViewPresentation) item.get(position);

        // get the TextView for item name and item description


        //sets the text for item name and item description from the current item object
        holder.textViewItemName.setText(currentItem.getModel().contactName);

        View finalConvertView = holder.itemView;
        holder.itemView.setOnLongClickListener(view -> {
            longClickListener.onLongClick(currentItem);
            finalConvertView.showContextMenu();
            return true;
        });
        holder.itemView.setOnClickListener(view -> {
            clickListener.onClick(currentItem);
            return;
        });
        CheckBox checkBox = holder.itemView.findViewById(R.id.check_box);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(currentItem.isSelected());
        checkBox.setVisibility(isAnySelected ? View.VISIBLE : View.INVISIBLE);
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            longClickListener.onLongClick(currentItem);
        });
        holder.itemView.setOnCreateContextMenuListener((contextMenu, view, contextMenuInfo) -> {
            contextMenu.add(finalConvertView.getContext().getString(R.string.menu_copy)).setOnMenuItemClickListener(menuItem -> {
                contextMenuListener.copy(currentItem);
                return true;
            });
            contextMenu.add(finalConvertView.getContext().getString(R.string.menu_remove)).setOnMenuItemClickListener(menuItem -> {
                contextMenuListener.remove(currentItem);
                return true;
            });
            contextMenu.add(finalConvertView.getContext().getString(R.string.menu_favorite)).setOnMenuItemClickListener(menuItem -> {
                contextMenuListener.favorite(currentItem);
                return true;
            });
        });
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewItemName ;
        ViewHolder(View view){
            super(view);
            textViewItemName = (TextView)
                    view.findViewById(R.id.text);
        }
    }
}