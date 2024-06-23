package com.example.registerotp.fragments;

import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;

import com.example.registerotp.R;
import com.example.registerotp.databinding.FragmentLoginBinding;
import com.example.registerotp.databinding.FragmentRegistrierenPhoneBinding;
import com.example.registerotp.model.KundenModell;
import com.example.registerotp.utils.AndroidUtil;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.Locale;

public class Fragment_registrieren_phone extends Fragment {
    private NavController navController;
    private FragmentRegistrierenPhoneBinding binding;
    private NestedScrollView nestedScrollView;
    private Fragment_registrieren_phone fragmentRegistrierenPhone;
    private CountryCodePicker ccp;
    private EditText editTextCarrierNumber;
    private KundenModell kundenModell;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Раздуваем макет
        binding = FragmentRegistrierenPhoneBinding.inflate(inflater, container, false);
        //возвращает корневое представление этого макета, чтобы система Android могла отобразить его

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        //Instanz der KundenModell-Klasse
        kundenModell = new KundenModell();

        binding.inputUsername.setOnClickListener(clickedView ->{
            binding.txtUsername.setHint(null);
        });

        fragmentRegistrierenPhone = this;
        Locale primaryLocale = fragmentRegistrierenPhone.getResources().getConfiguration().getLocales().get(0);
        String country = primaryLocale.getCountry();


        ccp = (CountryCodePicker) binding.ccp;
        editTextCarrierNumber = (EditText) binding.inputPhoneNumber;
        ccp.setCountryForNameCode(country);
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);

        //устанавливает слушатель нажатий
        binding.smsActivationBtn.setOnClickListener(clickedView  -> {
            //После нажатия отправляем по destination из Navigation
            if(isValid()){
                kundenModell.setUsername(binding.txtUsername.getEditText().getText().toString());
                kundenModell.setPhone(binding.ccp.getFullNumberWithPlus());

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                // Формируем запрос к коллекции "users" для проверки наличия пользователя с данным номером телефона
                db.collection("users")
                        .whereEqualTo("phone", binding.ccp.getFullNumberWithPlus())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Проверяем, есть ли документы в результате запроса
                                if (!task.getResult().isEmpty()) {
                                    // Пользователь с таким номером телефона найден регистрация не возможна!
                                    AndroidUtil.showToast(getActivity(), getString(R.string.invalid_phone_ready_registr));
                                } else {
                                    // Пользователь с таким номером телефона не найден, перенаправляем на регистрацию
                                    Bundle args = new Bundle();
                                    args.putBoolean("newUser", true);
                                    args.putParcelable("kundenModell", kundenModell);
                                    navController.navigate(R.id.id_action_to_sms_befor_regestrierung,args);

                                }
                            } else {
                                // Ошибка при выполнении запроса
                                Log.e("TAG", "Error getting documents: ", task.getException());
                            }
                        });


            }
        });

        // Устанавливаем слушатель изменения WindowInsets для вашего представления
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            // Получаем доступ к NestedScrollView из привязанного представления
            nestedScrollView = binding.nestedScrollViewGetSMS;

            // Определяем, видима ли в данный момент клавиатура
            boolean imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime());
            // Получаем высоту клавиатуры
            int imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;

            // Изменяем внутренние отступы NestedScrollView в зависимости от видимости клавиатуры
            animateNestedScrollViewPaddingChange(nestedScrollView, imeVisible ? imeHeight : 0);

            // Возвращаем insets, чтобы они могли быть потреблены другими представлениями
            return insets;
        });

    }

    //Methode zur Animation der Änderung des unteren Paddings
    private void animateNestedScrollViewPaddingChange(NestedScrollView nestedScrollView, int padding) {

        // Animiert die Änderung des Paddings
        ValueAnimator animator = ValueAnimator.ofInt(nestedScrollView.getPaddingBottom(), padding);
        //Setzt die Animationsdauer auf 100 Millisekunden.
        animator.setDuration(100);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                //Ändert das Padding während der Animation.
                nestedScrollView.setPadding(nestedScrollView.getPaddingLeft(), nestedScrollView.getPaddingTop(), nestedScrollView.getPaddingRight(), currentValue);
            }
        });
        animator.start();
    }

    private boolean isValid(){
        if(binding.txtUsername.getEditText().getText().toString().equals("")|| binding.txtUsername.getEditText().getText().toString().length() < 4){
            binding.txtUsername.setError(getString(R.string.invalid_name));
            return false;
        }else{
            binding.txtUsername.setError(null);
        }
        if(!binding.ccp.isValidFullNumber()) {
            binding.txtUserTelephon.setError(getString(R.string.invalid_telefon));
            return false;
        }else{
            binding.txtUserTelephon.setError(null);
        }
        return true;
    }

}