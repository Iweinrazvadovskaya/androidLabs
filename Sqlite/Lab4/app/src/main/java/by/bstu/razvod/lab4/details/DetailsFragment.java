package by.bstu.razvod.lab4.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import by.bstu.razvod.lab4.R;
import by.bstu.razvod.lab4.addcontact.AddContactActivity;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailsFragment extends Fragment {

    private DetailsViewModel viewModel;

    private static String ARG_LONG = ":arg:id";

    public static Fragment newInstance(Long id) {
        Bundle bundle = new Bundle();
        Fragment fragment = new DetailsFragment();
        bundle.putLong(ARG_LONG, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Long id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = requireArguments().getLong(ARG_LONG);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView phoneNumber = (TextView) view.findViewById(R.id.phone);
        TextView email = (TextView) view.findViewById(R.id.mail);
        TextView socialLink = (TextView) view.findViewById(R.id.slink);
        TextView location = (TextView) view.findViewById(R.id.location);
        Button createContactBtn = (Button) view.findViewById(R.id.createContact);
        viewModel.initialize(id);
        viewModel.getContact()
                .observe(getViewLifecycleOwner(), contactModel -> {
                    if (contactModel == null) return;
                    name.setText(contactModel.getContactName());
                    phoneNumber.setText(contactModel.getPhoneNumber());
                    email.setText(contactModel.getContactEmail());
                    location.setText(contactModel.getContactLocation());
                    socialLink.setText(contactModel.getLinkSocialNetwork());

                    location.setOnClickListener(view12 -> {
                        String geo = "geo:0,0?q=" + location.getText().toString();
                        Uri geoUri = Uri.parse(geo);
                        Intent intent = new Intent(Intent.ACTION_VIEW, geoUri);
                        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
                            startActivity(intent);
                        }

                        startActivity(intent);
                    });
                    createContactBtn.setOnClickListener(view1 -> {
                        Intent intent = new Intent(requireActivity(), AddContactActivity.class);
                        intent.putExtra("id", contactModel.getContactID());
                        startActivity(intent);
                        requireActivity().finish();
                    });
                });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

}
