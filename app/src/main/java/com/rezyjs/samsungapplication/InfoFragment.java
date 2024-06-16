package com.rezyjs.samsungapplication;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;


public class InfoFragment extends Fragment {

    // Автоматически сгенерированная часть кода
    // для фрагмента
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public InfoFragment() {}

    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    // Мой код, для реализации фрагмента
    // который отвечает за отрисовку графика и деталей

    // Декларация RecyclerView объекта
    RecyclerView recyclerView;
    // Декларация объекта Пирогового Графика
    PieChart pc_pieChart;
    // Декларация объекта, который помогает
    // пользоваться БД
    DBHelper DB;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Создаём новый объект для работы с БД
        DB = new DBHelper(this.requireContext());

        // Настраиваем график и детали в блоке ниже
        setPieChartAndDetails();
    }

    synchronized public void setPieChartAndDetails() {

        // Получаем данные нашего графика:
        // Положительные и отрицательные
        // - Проценты
        // - Остатки
        String positivePercentage = getPercentage("Plus") + "";
        String positiveLeftover = getLeftover("Plus") + "";
        String negativePercentage = getPercentage("Minus") + "";
        String negativeLeftover = getLeftover("Minus") + "";

        // Список всех операций
        List<Item> items = new ArrayList<>();

        // В новом потоке обращаемся к БД и получаем все уже известные нам операции
        (new Thread(() -> getAllAlreadyAddedData(items))).start();

        // Настраиваем RecyclerView
        recyclerView = requireView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new MyAdapter(this.getContext(), items));

        // Находим объект графика
        pc_pieChart = requireView().findViewById(R.id.piechart);

        // Добавляем 2 куска: Остаток и Траты
        pc_pieChart.addPieSlice(
                new PieModel(
                        "Остаток (" + positivePercentage + "%): " + positiveLeftover + " ₽",
                        getPercentage("Plus"),
                        Color.parseColor("#66BB6A")));
        pc_pieChart.addPieSlice(
                new PieModel(
                        "Траты (" + negativePercentage + "%): " + negativeLeftover + " ₽",
                        getPercentage("Minus"),
                        Color.parseColor("#EF5350")));

        // Устанавливаем значение текста внутри
        // графика как: "$$$"
        pc_pieChart.setInnerValueString("$$$");

        // Анимируем график
        pc_pieChart.startAnimation();
    }

    // Функция получения Остатка
    private float getLeftover(@NonNull String type) {

        // Траты
        float result = 0f;

        // Получаем все траты
        Cursor cursor = DB.getLosses();
        while (cursor.moveToNext()) {
            result += Float.parseFloat(cursor.getString(2));
        }

        if (type.equals("Plus")) {
            // Если нужен остаток, то
            // Вернуть разницу между
            // поступлениями и тратами

            // Все поступления
            float allMoney = 0f;

            // Получаем все поступления
            cursor = DB.getAdditions();
            while (cursor.moveToNext()) {
                allMoney += Float.parseFloat(cursor.getString(2));
            }

            return allMoney - result;
        } else if (type.equals("Minus")) {
            // Если нужны траты, то
            // Вернуть траты
            return result;
        } else {
            return 0f;
        }
    }

    // Функция рассчёта процентов
    private float getPercentage(@NonNull String type) {

        // Все поступления
        float allMoney = 0f;

        // Получаем все поступления
        Cursor cursor = DB.getAdditions();
        while (cursor.moveToNext()) {
            allMoney += Float.parseFloat(cursor.getString(2));
        }

        // В зависимости от запроса (Остаток, Трата) получаем:
        // 100 * запрос / Все поступления
        if (type.equals("Plus")) {
            return Math.round(100 * getLeftover("Plus") / allMoney);
        } else if (type.equals("Minus")) {
            return Math.round(100 * getLeftover("Minus") / allMoney);
        } else {
            return 0f;
        }
    }

    // Функция получения уже известной информации
    // Для отображения в блоке под графиком
    public void getAllAlreadyAddedData(List<Item> items) {
        // Получаем все операции
        Cursor cursor = DB.getAllOperations();
        // Перебираем все полученные операции и добавляем в таблицу
        while (cursor.moveToNext()) {
            items.add(new Item(cursor.getString(1), cursor.getString(2) + " ₽"));
        }
    }
}