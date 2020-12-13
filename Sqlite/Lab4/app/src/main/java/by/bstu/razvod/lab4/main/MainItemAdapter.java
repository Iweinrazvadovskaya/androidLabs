package by.bstu.razvod.lab4.main;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import by.bstu.razvod.lab4.MainViewPresentation;
import by.bstu.razvod.lab4.R;
import by.bstu.razvod.lab4.extendes.ClickListener;
import by.bstu.razvod.lab4.extendes.ContextMenuListener;
import by.bstu.razvod.lab4.extendes.LongClickListener;

public class MainItemAdapter extends RecyclerView.ViewHolder {
    final TextView textViewItemName;

    MainItemAdapter(View view) {
        super(view);
        textViewItemName = (TextView)
                view.findViewById(R.id.text);
    }

    void bind(MainViewPresentation item, boolean isAnySelected, @NonNull LongClickListener longClickListener, @NonNull ClickListener clickListener, @NonNull ContextMenuListener contextMenuListener) {
        textViewItemName.setText(item.getModel().getContactName());

        View finalConvertView = itemView;
        itemView.setOnLongClickListener(view -> {
            longClickListener.onLongClick(item);
            finalConvertView.showContextMenu();
            return true;
        });
        itemView.setOnClickListener(view -> {
            clickListener.onClick(item);
            return;
        });
        CheckBox checkBox = itemView.findViewById(R.id.check_box);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(item.isSelected());
        checkBox.setVisibility(isAnySelected ? View.VISIBLE : View.INVISIBLE);
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            longClickListener.onLongClick(item);
        });
        itemView.setOnCreateContextMenuListener((contextMenu, view, contextMenuInfo) -> {
            contextMenu.add(finalConvertView.getContext().getString(R.string.menu_copy)).setOnMenuItemClickListener(menuItem -> {
                contextMenuListener.copy(item);
                return true;
            });
            contextMenu.add(finalConvertView.getContext().getString(R.string.menu_remove)).setOnMenuItemClickListener(menuItem -> {
                contextMenuListener.remove(item);
                return true;
            });
            contextMenu.add(finalConvertView.getContext().getString(R.string.menu_favorite)).setOnMenuItemClickListener(menuItem -> {
                contextMenuListener.favorite(item);
                return true;
            });
        });
    }
}