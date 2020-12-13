package by.bstu.razvod.lab4.extendes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import by.bstu.razvod.lab4.MainViewPresentation;
import by.bstu.razvod.lab4.R;
import by.bstu.razvod.lab4.model.ContactModel;

public class ListAdapter extends BaseAdapter {

    private Context context; //context
    private LongClickListener longClickListener;
    private ClickListener clickListener;
    private ContextMenuListener contextMenuListener;
    private ContactModel contactModel;

    public ListAdapter(Context context, List<MainViewPresentation> data, @NonNull LongClickListener longClickListener, @NonNull ClickListener clickListener, @NonNull ContextMenuListener contextMenuListener) {
        this.context = context;
        this.items = data;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
        this.contextMenuListener = contextMenuListener;
    }


    private List<MainViewPresentation> items; //data source of the list adapter

    private boolean isAnySelected = false;

    public void setItems(List<MainViewPresentation> m) {
        this.items = m;
        isAnySelected = false;
        items.stream().forEach(item -> {
            if (item.isSelected()) {
                isAnySelected = true;
                return;
            }
        });
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row, parent, false);
        }

        // get current item to be displayed
        MainViewPresentation currentItem = (MainViewPresentation) getItem(position);

        // get the TextView for item name and item description
        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.text);

        //sets the text for item name and item description from the current item object
        textViewItemName.setText(currentItem.getModel().contactName);
        View finalConvertView = convertView;
        convertView.setOnLongClickListener(view -> {
            longClickListener.onLongClick(currentItem);
            finalConvertView.showContextMenu();
            return true;
        });
        convertView.setOnClickListener(view -> {
            clickListener.onClick(currentItem);
            return;
        });
        CheckBox checkBox = convertView.findViewById(R.id.check_box);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(currentItem.isSelected());
        checkBox.setVisibility(isAnySelected ? View.VISIBLE : View.INVISIBLE);
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            longClickListener.onLongClick(currentItem);
        });
        convertView.setOnCreateContextMenuListener((contextMenu, view, contextMenuInfo) -> {
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
        // returns the view for the current row
        return convertView;
    }
}
