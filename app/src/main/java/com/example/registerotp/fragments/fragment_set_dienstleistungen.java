package com.example.registerotp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;


import com.example.registerotp.R;
import com.example.registerotp.databinding.FragmentAnfangBinding;
import com.example.registerotp.databinding.FragmentSetDienstleistungenBinding;
import com.example.common.model.FirmenModel;
import com.example.registerotp.utils.AndroidUtil;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class fragment_set_dienstleistungen extends Fragment {

    private FragmentSetDienstleistungenBinding binding;
    private TextInputEditText eingabefirmenNameEditText;
    private FlexboxLayout chipGroupProfessions;
    private FlexboxLayout chipGroupKeywords;
    private FlexboxLayout selectedProfessionsChipGroup;
    private List<String> professionsList = new ArrayList<>();
    private List<String> selectedProfessionsSet = new ArrayList<>();
    private FirmenModel firmenmodell;
    private NavController navController;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firmenmodell = new FirmenModel();
        Bundle bundle = getArguments();
        firmenmodell = bundle.getParcelable("firmenModell");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Раздуваем макет
        binding = FragmentSetDienstleistungenBinding.inflate(inflater, container, false);
        //возвращает корневое представление этого макета, чтобы система Android могла отобразить его
        getProfessions();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        eingabefirmenNameEditText = binding.eingabefirmenName;
        chipGroupProfessions = binding.chipGroupProfessions;
        selectedProfessionsChipGroup = binding.selectedProfessionsChipGroup;
        // Инициализация карты с профессиями (ключ - название профессии, значение - описание профессии)
        eingabefirmenNameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Фильтруем карту профессий по введенному тексту
                filterProfessions(charSequence.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // Показать все ключевые слова при старте
        //filterProfessions("");

        binding.weiterToKeywordsBtn.setOnClickListener(v -> {
            if(!selectedProfessionsSet.isEmpty()){
                firmenmodell.setProfession(selectedProfessionsSet);
                Bundle args = new Bundle();
                args.putParcelable("firmenModell",firmenmodell);
                navController.navigate(R.id.id_action_to_set_keywords, args);
            }else {
                AndroidUtil.showToast(getActivity(), getString(R.string.empty_profession_secected));
            }
        });


    }
    private void filterProfessions(String input) {
        chipGroupProfessions.removeAllViews(); // Очищаем ChipGroup для профессий

        for (String entry : professionsList) {
            String professionName = entry;
            if (professionName.toLowerCase().contains(input.toLowerCase()) && !selectedProfessionsSet.contains(professionName) ) {
                addChipToGroup(professionName, chipGroupProfessions, true);
            }
        }
    }

    private void filterKeywords(String profession) {
        chipGroupKeywords.removeAllViews(); // Очищаем ChipGroup для ключевых слов

        /*List<String> keywords = professionsMap.get(profession);
        if (keywords != null) {
            for (String keyword : keywords) {
                addChipToGroup(keyword, chipGroupKeywords, false);
            }
        }*/
    }

    private void addChipToGroup(String text, FlexboxLayout chipGroup, boolean isProfession) {
        Chip chip = new Chip(getLayoutInflater().inflate(R.layout.single_chip_layout,chipGroup,false).getContext());
        chip.setChipBackgroundColorResource(R.color.background_chip);
        chip.setChipStrokeColorResource(R.color.background_chip);
        chip.setText(text);
        chip.setCloseIconVisible(false); // Скрыть иконку закрытия

        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Перемещаем выбранную профессию в другой ChipGroup
                addChipToSeclectedGroup(text, selectedProfessionsChipGroup);
                // Добавляем профессию в set выбранных профессий
                selectedProfessionsSet.add(text);
                // Очищаем поле ввода
                eingabefirmenNameEditText.setText("");
                // Очищаем ChipGroup с профессиями
                chipGroupProfessions.removeAllViews();
                // Скрыть клавиатуру
                hideKeyboard();
                // Фильтруем и показываем ключевые слова для выбранной профессии
                //filterKeywords(text);
                /*if (isProfession) {
                    // Фильтруем и показываем ключевые слова для выбранной профессии
                    filterKeywords(text);
                } else {
                    // Действие при клике на ключевое слово
                    // Например, можно показать описание или выполнить другие действия
                }*/
            }
        });
        // Создаем параметры LayoutParams и задаем отступы
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
        );
        int margin = (int) getResources().getDimension(R.dimen.chip_margin);
        layoutParams.setMargins(margin, margin, margin, margin);
        chip.setLayoutParams(layoutParams);

        chipGroup.addView(chip); // Добавляем chip в ChipGroup
    }
    private void addChipToSeclectedGroup(String text, FlexboxLayout flexboxLayout){
        Chip chip = new Chip(getLayoutInflater().inflate(R.layout.single_chip_layout,flexboxLayout,false).getContext());
        chip.setCloseIconVisible(true);
        chip.setCloseIconTintResource(R.color.black);
        chip.setChipBackgroundColorResource(R.color.background_chip);
        chip.setChipStrokeColorResource(R.color.background_chip);
        chip.setText(text);
        // Устанавливаем действие при клике на иконку закрытия
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flexboxLayout.removeView(chip); // Удалить чип из FlexboxLayout при клике на иконку закрытия
                selectedProfessionsSet.remove(text);
            }
        });
        // Создаем параметры LayoutParams и задаем отступы
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
        );
        int margin = (int) getResources().getDimension(R.dimen.chip_margin);
        layoutParams.setMargins(margin, margin, margin, margin);
        chip.setLayoutParams(layoutParams);
        selectedProfessionsChipGroup.addView(chip); // Добавляем chip в ChipGroup
    }
    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }
    private void getProfessions(){
        FirebaseFirestore.getInstance().collection("professions")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String profession = document.getId();
                            professionsList.add(profession);
                        }
                    }
                }).addOnFailureListener(e -> {
                    System.out.println("errorE"+ e);
        });
    }
}