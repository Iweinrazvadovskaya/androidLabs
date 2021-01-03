package by.bstu.razvod.listtviewcontextapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ListAdapter extends BaseAdapter {

    private Context context; //context
    private ClickListener clickListener;
    private ContextMenuListener contextMenuListener;
    private String contactModel;

    public ListAdapter(Context context, List<MainViewPresentation> data, @NonNull ClickListener clickListener, @NonNull ContextMenuListener contextMenuListener) {
        this.context = context;
        this.items = data;
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
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
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

        MainViewPresentation currentItem = (MainViewPresentation) getItem(position);

        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.text);

        textViewItemName.setText(currentItem.getModel());
        View finalConvertView = convertView;
        convertView.setOnClickListener(view -> {
            clickListener.onClick(currentItem);
        });

        convertView.setOnCreateContextMenuListener((contextMenu, view, contextMenuInfo) -> {
            contextMenu.add(finalConvertView.getContext().getString(R.string.menu_copy)).setOnMenuItemClickListener(menuItem -> {
                contextMenuListener.edit(currentItem);
                return true;
            });
            contextMenu.add(finalConvertView.getContext().getString(R.string.menu_remove)).setOnMenuItemClickListener(menuItem -> {
                contextMenuListener.remove(currentItem);
                return true;
            });
        });
        return convertView;
    }
}

