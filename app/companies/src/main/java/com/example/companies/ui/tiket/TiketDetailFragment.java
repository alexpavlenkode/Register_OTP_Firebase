package com.example.companies.ui.tiket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.companies.R;
import com.example.companies.SharedViewModel;
import com.example.companies.adapter.Tiket;
import com.example.companies.databinding.FragmentTaskDetailBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TiketDetailFragment extends Fragment implements OnMapReadyCallback {
    private FragmentTaskDetailBinding binding;
    private NavController navController;
    private SharedViewModel sharedViewModel;
    private Tiket tiket;
    private GoogleMap mMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Раздуваем макет
        binding = FragmentTaskDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        TextView btnNavigate = binding.linckToStartRoute;
        Button btnStartChat = binding.kundenAnschreibenTaskDetail;

        btnNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMapsForNavigation();
            }
        });

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getUserProfile().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                Log.d("TiketDetailFragment", "RecyclerView adapter set " + profile.getChats().toString());
            }
        });

        sharedViewModel.getSelectedTiket().observe(getViewLifecycleOwner(), tiket -> {
            this.tiket = tiket;
            binding.taskTimeFullTaskDetail.setText(tiket.getTimeAgo());
            binding.taskViewsFullTaskDetail.setText(String.valueOf(tiket.getViews()));
            binding.tiketFullTitle.setText(tiket.getTitle());
            binding.taskDescriptionFullTaskDetail.setText(tiket.getDescription());
            binding.taskAdresseSchortTaskDetail.setText(tiket.getAdresse());
            if(tiket.getUrgency() == 1){
                binding.tiketFullRapidetly.setBackgroundResource(com.example.common.R.drawable.rounded_background_task_card_hot);
                binding.tiketFullUrgency.setImageResource(com.example.common.R.drawable.hot);
                binding.taskDistanceFullTaskDetail.setText(com.example.common.R.string.urgent);
                binding.taskDistanceFullTaskDetail.setTextColor(com.example.common.R.color.white);
            } else if (tiket.getUrgency() == 2) {
                binding.tiketFullRapidetly.setBackgroundResource(com.example.common.R.drawable.rounded_background_task_card_calender);
                binding.tiketFullUrgency.setImageResource(com.example.common.R.drawable.calendar);
                binding.taskDistanceFullTaskDetail.setText(com.example.common.R.string.tomorrow);
                binding.taskDistanceFullTaskDetail.setTextColor(com.example.common.R.color.black);
            } else if (tiket.getUrgency() == 3) {
                binding.tiketFullRapidetly.setBackgroundResource(com.example.common.R.drawable.rounded_background_task_card_notrap);
                binding.tiketFullUrgency.setImageResource(com.example.common.R.drawable.more);
                binding.taskDistanceFullTaskDetail.setText(com.example.common.R.string.permanent);
                binding.taskDistanceFullTaskDetail.setTextColor(com.example.common.R.color.black);
            }
            binding.tiketFullStartEnd.setText(tiket.getKvTime());
            binding.tiketFullStartEndDate.setText(tiket.getKvDate());
        });
        sharedViewModel.getCompanyLocation().observe(getViewLifecycleOwner(),location ->{
            if (location != null) {
                binding.tiketFullDistant.setText(tiket.getDistanceTo(location));
                //loadMapImage(binding.mapImageSchortTaskDetail,tiket.getGeopoint().getLatitude(),tiket.getGeopoint().getLongitude());
                //AIzaSyBMFuyc2YQu78iMNQpfG56SGr4dEAd65AM
            }
        });
        btnStartChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), com.example.companies.R.id.nav_host_fragment_activity_user);
                navController.navigate(com.example.companies.R.id.chatRoomFragment);

            }
        });
        setupClickListeners(binding);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;

            LatLng latLng = new LatLng(tiket.getGeopoint().getLatitude(), tiket.getGeopoint().getLongitude());

            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
        } catch (Exception e) {
            Log.e("MapError", "Error setting up map", e);

        }
    }


    private void setupClickListeners(FragmentTaskDetailBinding binding) {
        binding.myBackButtonTaskDetail.setOnClickListener(v -> {
            // Закрыть текущий фрагмент
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Дополнительный обработчик для TextView
        binding.backTextTaskDetail.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        /*// Инициализация и добавление SupportMapFragment программно
        SupportMapFragment mapFragment = new SupportMapFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.map, mapFragment).commit();*/
        // Программная инициализация и добавление SupportMapFragment
        if (savedInstanceState == null) {
            SupportMapFragment mapFragment = SupportMapFragment.newInstance();
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.map_container, mapFragment);
            fragmentTransaction.commit();
            mapFragment.getMapAsync(this);
        }

        // Убедитесь, что карта доступна перед вызовом getMapAsync
        //mapFragment.getMapAsync(this);
    }

    private void openGoogleMapsForNavigation() {
        // Формируем URL для открытия Google Maps с маршрутом
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + tiket.getLocation().getLatitude() +","+ tiket.getLocation().getLongitude());
        // Формируем Intent для открытия Google Maps
        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        intent.setPackage("com.google.android.apps.maps");
        // Проверяем, есть ли приложения, которые могут обработать этот Intent
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    // TODO: Позже нужно доработать отображение адреса в тикете! Либо адреса нет, тогда только район
    //
    private String getGoogleMapsAdresse(){
        return "";
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
