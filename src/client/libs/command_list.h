#include <utility>
#include "string_view"
#include "sstream"
#include "bits/stdc++.h"
#include "command.pb.h"
#include <stdexcept>
#include "google/protobuf/arena.h"

#ifndef TEST_COMMAND_LIST_H
#define TEST_COMMAND_LIST_H

#define WRONG_COM "wrong"

using namespace std;
typedef serialization:: Dragon (*func) (string &name, string &args);
bool script_mode = false;

//не нужны пояснения! команды сервер выполняет, мы тупо валидируем

bool is_empty_string(string_view  name) {
    return all_of(name.begin(), name.end(), [](char ch) {
        return (isspace(ch));
    });
}

bool is_graphic(string_view  name) {
    return all_of(name.begin(), name.end(), [](char ch) {
        return (isgraph(ch));
    });
}

bool has_letters(string_view name){
    return any_of(name.begin(), name.end(), [](char ch) {
        return (isalpha(ch));
    });
}

bool nothing(string &s){
    return true;
}


pair<string, bool> read_string(const string& message, bool null_valid, bool (*method)(string&)= [] (string&s) {return true;}){
    string result;
    while(true){
        if(!script_mode) cout << message << '\n';
        getline(cin, result);
        if(is_empty_string(result)){
            if(null_valid){
                return {"", false};
            }
            else{
                if(!script_mode)  cout << "Значение поля не может быть пустым!\n";
                continue;
            }
        }
        if((*method)(result)) return {result, true};
    }

}
template<typename T>
pair<T, bool> read_number(const string& message, bool null_valid, T (*method)(string&), T Min = std::numeric_limits<T>::min(), T Max = std::numeric_limits<T>::max()){
    string result;
    while (true) {
        if(!script_mode) cout << message << '\n';
        getline(cin, result);

        if (result.empty()) {
            if (null_valid) return {Min, false};
            else {
                if(!script_mode) cout << "Значение не может быть пустым!\n";
                continue;
            }
        }
        if(has_letters(result)){
            if(!script_mode) cout << "Введенное значение не является числом\n";
            continue;
        }
        try{
            T res =  (*method)(result);
            if(res >= Min && res <= Max) return {res, true};
            else continue;
            //return stof(result);
        }
        catch(invalid_argument& e){
            if(!script_mode) cout << "Вы ввели некорректное значение!\n";
        }
        catch(std::out_of_range& e){
            if(!script_mode) cout << "Введенное значение выходит за диапазон\n";
        }
    }
}
template<typename T>
pair<T, bool> read_enum(const string& message, bool null_valid, vector<string>names, T(*method)(string&)){
    string result;
    while(true){
        if(!script_mode) cout << message << '\n';
        if(!script_mode) for(auto &name : names) cout << name << '\n';
        getline(cin, result);
        if(is_empty_string(result)){
            if(null_valid){
                return{(*method)(names[0]), false};
            }
            else{
                if(!script_mode) cout << "Значение поля не может быть пустым!\n";
                continue;
            }
        }
        for(auto &name : names){
            if(name == result) return {(*method)(name), true};
        }
        if(!script_mode) cout << "Вы ввели некоректное значение!\n";
    }
}

double to_double(string &s){
    return stod(s);
}

float to_float(string &s){
    return stof(s);
}

int to_int(string &s){
    return stoi(s);
}

bool val_passport(string &s){
    return s.length() >= 9;
}

bool val_local_date(string &s){
    if(s.length() != 10) return false;
    for(int i = 0; i < s.length(); i++){
        if((i == 4 || i == 7)){
            if(s[i] != '-') return false;
        }
        else if(!(s[i] >= '0' && s[i] <= '9')) return false;
    }
    return true;
}


serialization::Dragon_Color is_color(string &s){
    if(s == "BLACK") return serialization:: Dragon_Color_BLACK;
    else if(s == "RED") return serialization:: Dragon_Color_RED;
    else if(s == "BLUE") return serialization:: Dragon_Color_BLUE;
    else return serialization:: Dragon_Color_WHITE;
}

serialization::Dragon_DragonType is_type(string &s){
    if(s == "WATER") return serialization:: Dragon_DragonType_WATER;
    else if(s == "UNDERGROUND") return serialization:: Dragon_DragonType_UNDERGROUND;
    else if(s == "AIR") return serialization:: Dragon_DragonType_AIR;
    else return serialization:: Dragon_DragonType_FIRE;
}

serialization::Dragon_DragonCharacter is_character(string &s){
    if(s == "CUNNING") return serialization:: Dragon_DragonCharacter_CUNNING;
    else if(s == "WISE") return serialization:: Dragon_DragonCharacter_WISE;
    else if(s == "GOOD") return serialization:: Dragon_DragonCharacter_GOOD;
    else return serialization:: Dragon_DragonCharacter_CHAOTIC;
}


void read_killer(serialization::Dragon &dragon){
    if(!script_mode) cout << "Ввод убийцы дракона. Нажмите Enter, если вы хотите оставить поле пустым или введите любой символ, чтобы приступить к заполнению полей\n";
    string s;
    getline(cin, s);
    if(s.empty()) return;

    auto killer_name = read_string("Введите имя убийцы дракона. Имя не может быть пустым!", false);
    if(killer_name.second) dragon.set_killer_name(killer_name.first);

    auto birthday = read_string("Введите значение в формате LocalDate: день рождения убийцы драконов. Чтобы пропустить ввод этого поля, нажмите Enter.", true, &val_local_date);
    if(birthday.second) dragon.set_killer_birthday(birthday.first);

    auto height = read_number<double>("Введите вещественное число: рост убийцы драконов. Чтобы пропустить ввод этого поля, нажмите Enter.", true, &to_double);
    if(height.second) dragon.set_killer_height(to_string(height.first));

    auto weight = read_number<float>("Введите вещественное число: вес убийцы дракона. Он должен быть больше 0. Чтобы пропустить ввод этого поля, нажмите Enter.", true, &to_float,  1);
    if(weight.second) dragon.set_killer_weight(to_string(weight.first));

    auto passport = read_string("Введите id паспорта. Длина id не может быть меньше 9! id должно быть уникальным. Чтобы пропустить ввод этого поля, нажмите Enter.", true, &val_passport);
    if(passport.second) dragon.set_killer_passport_id(passport.first);
}

void read_dragon(serialization:: Dragon &dragon){
    auto name = read_string("Введите имя дракона. Имя не может быть пустым!", false);
    if(name.second) dragon.set_name(name.first);


    auto x = read_number<double>("Введите вещественное число: x-координату дракона. Она не должна превышать 280.", true, &to_double, std::numeric_limits<double>::min(), 280);
    dragon.set_x(to_string(x.first));

    auto y = read_number<float>("Введите вещественное число: y-координату дракона.", true, &to_float);
    dragon.set_y(to_string(y.first));


    auto age = read_number<int>("Введите возраст дракона. Возраст должен быть больше 0. Это поле не может иметь значение null", false, &to_int, 1);
    if(age.second) dragon.set_age(age.first);

    pair<serialization::Dragon_Color, bool> color_res = read_enum<serialization::Dragon_Color>("Введите цвет дракона. Это поле можно пропустить, нажав Enter. Вы можете ввести одно из следующих значений:", true, {"BLACK","RED","BLUE","WHITE"}, &is_color);
    if(color_res.second) dragon.set_color(color_res.first);

    pair<serialization::Dragon_DragonType, bool> type = read_enum<serialization::Dragon_DragonType>("Введите тип дракона. Это поле можно пропустить, нажав Enter. Вы можете ввести одно из следующих значений:", true, {"WATER","UNDERGROUND","AIR","FIRE"}, &is_type);
    if(type.second) dragon.set_type(type.first);

    pair<serialization:: Dragon_DragonCharacter, bool> character = read_enum<serialization:: Dragon_DragonCharacter>("Введите характер дракона. Вы можете ввести одно из следующих значений:", false, {"CUNNING","WISE","GOOD","CHAOTIC"}, &is_character);
    if(character.second) dragon.set_character(character.first);

    read_killer(dragon);
}



serialization::Dragon no_args_val(string &name, string &arg){
    serialization:: Dragon command;
    if(!arg.empty()){
        if(!script_mode) cout << "Некорректный ввод: команда "<< name << " не должна содержать аргументы";
        command.set_comname(WRONG_COM);
        return command;
    }
    else{
        command.set_comname(name);
        return command;
    }
}

serialization::Dragon element_val(string &name, string& arg){
    serialization:: Dragon command;

    if(!arg.empty()){
        if(!script_mode) cout << "Некорректный ввод: команда "<< name << " должна содержать элемент коллекции в качестве аргумента\n";
        command.set_comname(WRONG_COM);

    }

    else{
        command.set_comname(name);
        read_dragon(command);
    }
    return command;
}

serialization:: Dragon string_val(string &name, string &arg) {
    serialization:: Dragon command;
    if(arg.empty()){
        cout << "Некорректный ввод: команда "<< name << " должна содержать один аргумент\n";
        command.set_comname(WRONG_COM);
    }
    else{
        command.set_comname(name);
        command.set_arguments(arg);
    }
    return command;
}

serialization::Dragon element_int_val(string &name, string &arg){
    serialization:: Dragon command = string_val(name, arg);
    read_dragon(command);
    return command;
}


struct command_manager{
    string filename;
    static serialization:: Dragon choose_command(vector<string>&user_command, bool mode){
        script_mode = mode;
        vector<pair<string, func>>names = {{"help", no_args_val},
                                           {"info", no_args_val},
                                           {"show", no_args_val},
                                           {"exit", no_args_val},
                                           {"add",  element_val},
                                           {"execute_script", string_val},
                                           {"update", element_int_val},
                                           {"remove_by_id", string_val},
                                           {"clear", no_args_val},
                                           {"add_if_min",element_val},
                                           {"remove_greater", element_val},
                                           {"remove_lower", element_val},
                                           {"sum_of_age", no_args_val},
                                           {"count_greater_than_character", string_val},
                                           {"filter_by_character", string_val}}; //корректный характер

        int amount = (int)names.size();
        string name = user_command[0];
        string arg = user_command.size() > 1 ? user_command[1] : "";
        for(int i = 0; i < amount; i++){
            if(names[i].first == name) return names[i].second(name, arg);
        }
        if(!script_mode) cout << "Данной команды не существует. Введите help, чтобы ознакомиться со списком команд.\n";
        serialization::Dragon command;
        command.set_comname(WRONG_COM);
        return command;

    }
};


#endif //TEST_COMMAND_LIST_H

