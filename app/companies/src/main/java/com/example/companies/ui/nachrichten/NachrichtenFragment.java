package com.example.companies.ui.nachrichten;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.companies.R;
import com.example.companies.SharedViewModel;
import com.example.companies.adapter.User;
import com.example.companies.adapter.UserAdapter;
import com.example.companies.databinding.FragmentNachrichtenBinding;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;

import java.util.ArrayList;
import java.util.List;

public class NachrichtenFragment extends Fragment {
    private SharedViewModel viewModel;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private FragmentNachrichtenBinding binding;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NachrichtenViewModel dashboardViewModel =
                new ViewModelProvider(this).get(NachrichtenViewModel.class);

        binding = FragmentNachrichtenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //// Настройка RecyclerView
        recyclerView = binding.recyclerViewTasks;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new ArrayList<>();
        userList.add(new User(
                getString(com.example.common.R.drawable.ic_user_placeholder), // URL фото
                true, // Онлайн
                "Иван Иванов", // Имя
                "Привет, как дела?", // Последнее сообщение
                "10:15", // Время отправки
                5, // Количество непрочитанных сообщений
                User.STATUS_ACTIVE, // Статус
                "123"
        ));
        userList.add(new User(
                getString(com.example.common.R.drawable.ic_user_placeholder), // URL фото
                false, // Не онлайн
                "Анна Петрова", // Имя
                "Когда встретимся?", // Последнее сообщение
                "12:30", // Время отправки
                2, // Количество непрочитанных сообщений
                User.STATUS_COMPLETED, // Статус
                "124"
        ));
        userList.add(new User(
                getString(com.example.common.R.drawable.ic_user_placeholder), // URL фото
                true, // Онлайн
                "Сергей Смирнов", // Имя
                "Увидел твое сообщение.", // Последнее сообщение
                "14:45", // Время отправки
                0, // Количество непрочитанных сообщений
                User.STATUS_ACTIVE, // Статус
                "125"
        ));
        userList.add(new User(
                getString(com.example.common.R.drawable.ic_user_placeholder), // URL фото
                true, // Онлайн
                "Василий Петров", // Имя
                "Увидел твое сообщение.", // Последнее сообщение
                "14:45", // Время отправки
                0, // Количество непрочитанных сообщений
                User.STATUS_ACTIVE, // Статус
                "126"
        ));
        userList.add(new User(
                getString(com.example.common.R.drawable.ic_user_placeholder), // URL фото
                true, // Онлайн
                "Ольга Михайловна", // Имя
                "Увидел твое сообщение.", // Последнее сообщение
                "14:45", // Время отправки
                11, // Количество непрочитанных сообщений
                User.STATUS_ACTIVE, // Статус
                "127"
        ));
        userList.add(new User(
                getString(com.example.common.R.drawable.ic_user_placeholder), // URL фото
                true, // Онлайн
                "Валерий Мамаев", // Имя
                "Увидел твое сообщение.", // Последнее сообщение
                "14:45", // Время отправки
                11, // Количество непрочитанных сообщений
                User.STATUS_ACTIVE, // Статус
                "128"
        ));

        // Создание и установка адаптера
        userAdapter = new UserAdapter(getContext(), userList);
        recyclerView.setAdapter(userAdapter);

        FlexboxLayout chipGroupWichtigkeitNachrichten = binding.chipGroupWichtigkeitNachrichten;
        FlexboxLayout selectedWichtigkeitNachrichten = binding.selectedChipGroupWichtigkeitNachrichten;
        
        addChipToGroup(chipGroupWichtigkeitNachrichten,getString(com.example.common.R.string.messages_all), ContextCompat.getColor(getContext(), com.example.common.R.color.white), com.example.common.R.color.messages_all);
        addChipToGroup(chipGroupWichtigkeitNachrichten,getString(com.example.common.R.string.messages_inprogress),ContextCompat.getColor(getContext(), com.example.common.R.color.black), com.example.common.R.color.messages_inprogress);
        addChipToGroup(chipGroupWichtigkeitNachrichten,getString(com.example.common.R.string.messages_completed),ContextCompat.getColor(getContext(), com.example.common.R.color.black), com.example.common.R.color.messages_completed);



        return root;
    }

    private void addChipToGroup(FlexboxLayout chipGroup, String text, int textColor,int iconColor) {
        Chip chip = new Chip(getActivity());
        chip.setText(text);
        chip.setClickable(true);
        chip.setCheckable(true);
        chip.setChipIconVisible(true);  // Ensure the icon is visible
        chip.setTextColor(textColor);
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
        int margin = (int) getResources().getDimension(com.example.common.R.dimen.chip_intent_margin);
        layoutParams.setMargins(margin, margin, margin, margin);
        chip.setLayoutParams(layoutParams);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}