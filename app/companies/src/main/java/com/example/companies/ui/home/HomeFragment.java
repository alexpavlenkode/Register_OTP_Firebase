package com.example.companies.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.companies.SharedViewModel;
import com.example.companies.adapter.Tiket;
import com.example.companies.adapter.TiketAdapter;
import com.example.companies.databinding.FragmentHomeBinding;
import com.example.companies.repository.Location;
import com.example.companies.repository.TaskRepository;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.chip.Chip;
import com.example.common.R;
import com.google.android.material.chip.ChipDrawable;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment: ";

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private TiketAdapter tiketAdapter;
    private List<Tiket> tiketList;
    private NavController navController;
    private SharedViewModel viewModel;
    private TaskRepository taskRepository;
    private List<String> professions;
    private LatLng userLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerViewTasks;
        taskRepository = new TaskRepository();

        tiketAdapter = new TiketAdapter(tiketList,userLocation, new TiketAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Tiket task) {
                NavController navController = Navigation.findNavController(getActivity(), com.example.companies.R.id.nav_host_fragment_activity_user);
                navController.navigate(com.example.companies.R.id.tiketDetailFragment);

            }
        });

        tiketList = new ArrayList<>();
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.fetchUserProfile();

        LatLng placeholderLocation = new LatLng(0.0, 0.0);
        userLocation = placeholderLocation;

        Location companyLocation = new Location();
        companyLocation.init(getContext(),getActivity());

        viewModel.setCompanyLocation(companyLocation.getMyLocation());
        viewModel.getCompanyLocation().observe(getViewLifecycleOwner(), location -> {
            if (location != null) {
                // Местоположение доступно
                userLocation = location;

            } else {
                // Используйте placeholder
                userLocation = placeholderLocation;
            }
        });


        FlexboxLayout chipGroupWichtigkeit = binding.chipGroupWichtigkeit;
        FlexboxLayout selectedWichtigkeitChipGroup = binding.selectedChipGroupWichtigkeit;

        // Add chips with text from strings.xml
        addChipToGroup(chipGroupWichtigkeit, getString(R.string.urgent), R.drawable.hot, R.color.urgent_background, 1);
        addChipToGroup(chipGroupWichtigkeit, getString(R.string.tomorrow), R.drawable.hot_tomorrow,R.color.tomorrow_background, 2);
        addChipToGroup(chipGroupWichtigkeit, getString(R.string.permanent), R.drawable.hot_forever,R.color.permanent_background, 3);

        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        // Наблюдение за изменениями в профиле
        viewModel.getUserProfile().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                fetchTasksByProfession();
            }
        });

        // Инициализация ViewModel с контекстом
        //viewModel.init(getContext(),getActivity());
        //viewModel.getUserLocation();



        recyclerView.setAdapter(tiketAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(tiketAdapter);


    }

    private void fetchTasksByProfession() {
        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.getUserProfile().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                List<String> professions = profile.getProfessions();
                if (professions != null && !professions.isEmpty()) {
                    TaskRepository taskRepository = new TaskRepository(); // Инициализация вашего репозитория
                    taskRepository.getTasksByProfessions(professions, new TaskRepository.OnTasksFetchedListener() {
                        @Override
                        public void onTasksFetched(List<Tiket> tasks) {
                            // Обновляем список задач
                            tiketList.clear();
                            tiketList.addAll(tasks);
                            if (tiketAdapter != null) {

                                updateTask(tasks);
                            } else {
                                Log.e("HomeFragment", "TiketAdapter is null");
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("HomeFragment", "Error fetching tasks: " + e.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void updateTask(List<Tiket> tiketList){
        TiketAdapter tiketAdapter = new TiketAdapter(tiketList,userLocation, new TiketAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Tiket tiket) {
                viewModel.selectTiket(tiket);
                NavController navController = Navigation.findNavController(getActivity(), com.example.companies.R.id.nav_host_fragment_activity_user);
                navController.navigate(com.example.companies.R.id.tiketDetailFragment);
            }
        });
        recyclerView.setAdapter(tiketAdapter);

    }


    private void filterTasksByUrgency(int urgency){
        List<Tiket> filteredTasks = new ArrayList<>();
        for (Tiket tiket : tiketList) { // taskList - это полный список задач
            if (tiket.getUrgency() == urgency) {
                filteredTasks.add(tiket);
            }
        }
        updateTask(filteredTasks);
    }

    private void addChipToGroup(FlexboxLayout chipGroup, String text,int iconResId, int iconColor, int urgency) {
        Chip chip = new Chip(getActivity());
        chip.setText(text);
        chip.setClickable(true);
        chip.setCheckable(true);
        chip.setChipIconResource(iconResId);
        chip.setChipIconVisible(true);  // Ensure the icon is visible
        chip.setChipBackgroundColorResource(iconColor);

        ChipDrawable chipDrawable = (ChipDrawable) chip.getChipDrawable();
        if (chipDrawable != null) {
            chipDrawable.setChipCornerRadius(40f); // Устанавливаем радиус закругления в пикселях
            chipDrawable.setChipStrokeColorResource(android.R.color.transparent); // Убираем обводку

        }
        chip.setChipDrawable(chipDrawable);

        // Установка обработчика нажатий
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterTasksByUrgency(urgency);
            }
        });

        chipGroup.addView(chip);
        // Создаем параметры LayoutParams и задаем отступы
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
        );
        int margin = (int) getResources().getDimension(R.dimen.chip_intent_margin);
        layoutParams.setMargins(margin, margin, margin, margin);
        chip.setLayoutParams(layoutParams);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}