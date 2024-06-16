package com.rezyjs.samsungapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangesFragment extends Fragment {

    // Автоматически сгенерированная часть кода
    // для фрагмента

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ChangesFragment() {
        // Required empty public constructor
    }

    public static ChangesFragment newInstance(String param1, String param2) {
        ChangesFragment fragment = new ChangesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_changes, container, false);
    }

    // Мой код, для реализации фрагмента
    // который отвечает за добавление операций

    // Массив, хранящий варианты для выпадающего меню
    String[] items = {"Пополнение", "Трата"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    // Декларация кнопки добавления
    Button btn_addToDB;
    // Декларация объекта EditText, в который
    // пользователь пишет количество денег
    EditText et_money;
    // Декларация переменной, которая хранит операцию
    String operation = "";
    // Декларация объекта, который помогает
    // пользоваться БД
    DBHelper DB;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapterItems = new ArrayAdapter<>(this.requireContext(), R.layout.list_item, items);

        // Находим наше выпадающее меню,
        // устанавливаем ему адаптер
        autoCompleteTextView = requireView().findViewById(R.id.auto_complete_text);
        autoCompleteTextView.setAdapter(adapterItems);

        // Присваиваем слушатель по нажатию:
        // Получаем значение, выбранное пользователем
        // в нашем выпадающем меню
        autoCompleteTextView.setOnItemClickListener(
                (parent, view1, position, id)
                        -> operation = parent.getItemAtPosition(position).toString()
        );

        // Находим кнопку добавить и
        // EditText с количеством денег
        btn_addToDB = requireView().findViewById(R.id.btn_add);
        et_money = requireView().findViewById(R.id.et_money);

        // Создаём новый объект для работы с БД
        DB = new DBHelper(this.requireContext());

        // Устанавливаем новый слушатель по нажатию для кнопки:
        // - Пытаемся добавить данные в БД
        // - - Если не вышло, то пишем о неудаче
        // - - Если вышло, то пишем об успехе
        btn_addToDB.setOnClickListener(v -> {
            Boolean check = DB.insertData(operation, et_money.getText().toString());

            if (!check) {
                Toast.makeText(ChangesFragment.this.requireContext(), "Ошибка ввода данных!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChangesFragment.this.requireContext(), "Успех!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}