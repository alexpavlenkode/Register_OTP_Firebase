package com.example.companies.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.companies.adapter.Task;
import com.example.companies.adapter.TaskAdapter;
import com.example.companies.databinding.FragmentHomeBinding;
import com.example.companies.ui.task.TaskDetailFragment;
import com.example.companies.ui.task.TaskDetailViewModel;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;
import com.example.comon.R;
import com.google.android.material.chip.ChipDrawable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private NavController navController;

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

        FlexboxLayout chipGroupWichtigkeit = binding.chipGroupWichtigkeit;
        FlexboxLayout selectedWichtigkeitChipGroup = binding.selectedChipGroupWichtigkeit;


        // Add chips with text from strings.xml
        addChipToGroup(chipGroupWichtigkeit, getString(R.string.urgent), R.drawable.hot, R.color.urgent_background);
        addChipToGroup(chipGroupWichtigkeit, getString(R.string.tomorrow), R.drawable.hot_tomorrow,R.color.tomorrow_background);
        addChipToGroup(chipGroupWichtigkeit, getString(R.string.permanent), R.drawable.hot_forever,R.color.permanent_background);

        recyclerView = binding.recyclerViewTasks;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        taskList = new ArrayList<>();

        // Add tasks to the list
        taskList.add(new Task(R.drawable.hot, "Urgent Task", "This is a short description of the task.", 10, new Date(), "44 km"));
        taskList.add(new Task(R.drawable.hot, "Tomorrow Task", "Another short description of the task.", 5, new Date(), "58 km"));
        taskList.add(new Task(R.drawable.hot, "Tomorrow Task", "Lorem ipsum dolor sit amet consectetur. Placerat quis ut fames morbi commodo interdum vulputate morbi at. Lorem ipsum dolor sit amet consectetur. Placerat quis ut fames morbi commodo interdum vulputate morb...", 5, new Date(), "58 km"));
        taskList.add(new Task(R.drawable.hot, "Tomorrow Task", "Another short description of the task.", 5, new Date(), "58 km"));
        taskList.add(new Task(R.drawable.hot, "Tomorrow Task", "Another short description of the task.", 5, new Date(), "58 km"));
        taskList.add(new Task(R.drawable.hot, "Tomorrow Task", "Lorem ipsum dolor sit amet consectetur. Placerat quis ut fames morbi commodo interdum vulputate morbi at. Lorem ipsum dolor sit amet consectetur. Placerat quis ut fames morbi commodo interdum vulputate morb...", 5, new Date(), "58 km"));



        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        TaskAdapter taskAdapter = new TaskAdapter(taskList, new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                NavController navController = Navigation.findNavController(getActivity(), com.example.companies.R.id.nav_host_fragment_activity_user);
                navController.navigate(com.example.companies.R.id.taskDetailFragment);


            }
        });
        Log.d("HomeFragment", "RecyclerView adapter set");
        recyclerView.setAdapter(taskAdapter);

    }

    private void addChipToGroup(FlexboxLayout chipGroup, String text,int iconResId, int iconColor) {
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