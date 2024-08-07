package com.example.registerotp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.registerotp.R;
import com.example.registerotp.databinding.FragmentSetKeywordsBinding;
import com.example.common.model.FirmenModel;
import com.example.registerotp.utils.AndroidUtil;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class fragment_set_keywords extends Fragment {
    private FirmenModel firmenmodell;
    private FragmentSetKeywordsBinding binding;
    private FirebaseAuth mAuth;
    private NavController navController;
    private List<String> allKeywordsList = new ArrayList<>();
    private List<String> selectedProfessions;
    private FlexboxLayout chipGroupKeywords;
    private FlexboxLayout selectedKeywordsChipGroup;
    private List<String> selectedKeywordsSet = new ArrayList<>();


    /**
     * 1. После onCreateView генерируются все ключевые слова в getKeywords()
     *
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        firmenmodell = bundle.getParcelable("firmenModell");

        /*//Это можно убрать !!!
        firmenmodell = new FirmenModel();
        String demo = "Bauleiter";
        Set<String> demoSet = Collections.singleton(demo);
        firmenmodell.setProfession(demoSet);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSetKeywordsBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        //Загружаем групу чипсов
        chipGroupKeywords = binding.chipGroupKeywords;
        chipGroupKeywords.removeAllViews();
        //Получаем все ключевые слова
        getKeywords();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        chipGroupKeywords.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        binding.weiterToEmailPassword.setOnClickListener(v -> {
            if(selectedKeywordsSet.isEmpty()){
                List<String> keywordsSet = new ArrayList<>(allKeywordsList);
                firmenmodell.setKeywordsProfession(keywordsSet);
            }else {
                firmenmodell.setKeywordsProfession(selectedKeywordsSet);
            }
            Bundle args = new Bundle();
            args.putParcelable("firmenModell", firmenmodell);
            navController.navigate(R.id.id_action_to_set_mail_pass, args);

        });


    }

    private void addChipToGroup(String text, FlexboxLayout chipGroupKeywords) {
        Chip chip = new Chip(getLayoutInflater().inflate(R.layout.single_chip_layout,chipGroupKeywords,false).getContext());
        chip.setChipBackgroundColorResource(R.color.background_chip_keywords);
        chip.setChipStrokeColorResource(R.color.background_chip_keywords_selected);
        chip.setText(text);
        chip.setCheckable(true);
        chip.setCloseIconVisible(false); //иконку закрытия
        /*chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Перемещаем выбранную профессию в другой ChipGroup
                chip.setChipBackgroundColorResource(R.color.background_chip_selected);
                chip.setChipStrokeColorResource(R.color.background_chip_selected);
                // Добавляем профессию в set выбранных профессий
                selectedKeywordsSet.add(text);

            }

        });*/
        //Убираем или Добавляем выбранные Ключевые слова
        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chip.setChipBackgroundColorResource(R.color.background_chip_selected);
                    chip.setChipStrokeColorResource(R.color.background_chip_selected);
                    selectedKeywordsSet.add(text);
                }
                if (!isChecked){
                    // Перемещаем выбранную профессию в другой ChipGroup
                    chip.setChipBackgroundColorResource(R.color.background_chip_keywords);
                    chip.setChipStrokeColorResource(R.color.background_chip_keywords_selected);
                    // Добавляем профессию в set выбранных профессий
                    selectedKeywordsSet.remove(text);
                }
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

        chipGroupKeywords.addView(chip); // Добавляем chip в ChipGroup
    }

    private void getKeywords(){
        //selectedProfessions = firmenmodell.getProfessions();
        selectedProfessions = firmenmodell.getProfessions();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (String profession : selectedProfessions) {
            db.collection("professions").document(profession)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            List<String> keywords = (List<String>) documentSnapshot.get("keywords");
                            if (keywords != null) {
                                allKeywordsList.addAll(keywords);
                                for (String keyword : allKeywordsList) {
                                    addChipToGroup(keyword, chipGroupKeywords);
                                }
                            }
                        }
                    }).addOnFailureListener(e -> {
                        System.out.println("errorE"+ e);
            });
        }
    }
}
