#include "bits/stdc++.h"
#include "command.pb.h"
#include "command_list.h"
#include<unistd.h>
#include<fcntl.h>
#include "filesystem"

#ifndef TEST_INTERACTOR_H
#define TEST_INTERACTOR_H
#define EXECUTE "execute_script"
#define EXIT "exit"
using namespace std;
using namespace filesystem;

class Interactor{

private:string start_message = "Введите любую команду или help для получения списка всех команд.\n";
private: int obj_socket, stdin_fileno{}, input_fds{};
private: bool finish_interaction, script_mode;

    void process_input(string &user_input){
        vector<string>command;
        string buf;
        for(auto &i : user_input){
            if(i == ' '){
                if(!buf.empty()) command.emplace_back(buf);
                buf = "";
            }
            else buf += i;
        }
        if(!buf.empty()) command.emplace_back(buf);
        if(command.empty() && !script_mode) cout << "Вы ввели пустую команду!\n";
        else {
            if(!script_mode){
                cout << "Вы ввели:\n";
                for(auto &commands : command) cout << commands << ' ';
                cout << '\n';
            }

            check_command(command);
        }
    }


    static bool IsReadable(const path& p)
    {
        error_code ec; // For noexcept overload usage.
        auto perms = status(p, ec).permissions();
        if ((perms & perms::owner_read) != perms::none &&
            (perms & perms::group_read) != perms::none &&
            (perms & perms::others_read) != perms::none)
        {
            return true;
        }
        return false;
    }


    void divide_script(serialization:: Dragon &dragon){
        path filePath(dragon.arguments());
        error_code ec; // For noexcept overload usage.
        if (exists(filePath, ec) && !ec)
        {
            if (IsReadable(filePath))
            {
                string p = filePath;
                const char * c = p.c_str();
                input_fds = open(c, O_RDONLY);
                ofstream fout(c, std::ios::app);
                fout << "\n@\n";
                fout.close();
                if(dup2(input_fds, STDIN_FILENO) < 0) {
                    printf("Невозможно дублировать дескриптор файла.");
                    exit(EXIT_FAILURE);
                }
                script_mode = ~script_mode;

            }
            else cout << "Недостаточно прав для доступа к файлу." << endl;
        }
        else cout << "Данного файла не существует." << endl;
    }

     void check_command(vector<string>&command){
        serialization:: Dragon dragon = command_manager::choose_command(command, script_mode);
        if(dragon.comname() == WRONG_COM) return;
        else if(dragon.comname() == EXECUTE){
            divide_script(dragon);
        }
        else if(dragon.comname() == EXIT){
            finish_interaction = true;
            cout << "Завершение работы клиента!\n";
            return;
        }
        else send_command(dragon);
    }

    void get_response() const{
        //int connect( obj_socket, (struct sockaddr *)&serv_addr, sizeof(serv_addr )) < 0
        char buffer[4096] = {0};
        recv(obj_socket, buffer, 4096, 0);
        string a = buffer;

        serialization::Response response;
        response.ParseFromString(a);
        if(response.has_response()) cout << response.response() << '\n';
        if(response.has_collection()) printDragons(response);
    }

     void send_command(serialization::Dragon &dragon) const{
        cout << "Отправляю команду на сервер...\n";
        string data;
        dragon.SerializeToString(&data);
        char requestBuffer[data.length()];
        sprintf(requestBuffer, "%s", data.c_str());
        send(obj_socket, requestBuffer, sizeof(requestBuffer), 0);

        get_response();

    }


    static void printDragons(serialization::Response &response){
        if(response.has_collection()){
            const serialization::DragonCollection& collection = response.collection();
            for(auto &i : collection.dragons()){
                cout << "~~~~~~~~~~~~~~~~~~~(*_*)\n";
                cout << "id: " << i.id() << endl;
                if(i.has_x() && i.has_y()) cout << "координаты: " << i.x() << ' ' << i.y() << endl;
                cout << "имя: " << i.name() << endl;
                cout << "возраст: " << i.age() << endl;
                if(i.has_color()) cout << "цвет: "  << i.color() << endl;
                if(i.has_type()) cout << "тип: " << i.type() << endl;
                cout << "характер: " << i.character() << endl;
                if(i.has_killer_name()){
                    cout << "\nУбийца дракона! (`^ _ ^`) " << i.killer_name() << endl;
                    if(i.has_killer_birthday()) cout << "дата рождения убийцы дракона: " << i.killer_birthday() << endl;
                    if(i.has_killer_height()) cout << "рост убийцы дракона: " << i.killer_height() << endl;
                    if(i.has_killer_weight()) cout << "вес убийцы дракона: " << i.killer_weight() << endl;
                    if(i.has_killer_passport_id()) cout << "паспорт убийцы дракона: " << i.killer_passport_id() << endl;

                }
                cout << "~~~~~~~~~~~~~~~~~~~" << endl;

            }
        }
    }

public:
    explicit Interactor(int obj_socket){
        this->obj_socket = obj_socket;
        this->finish_interaction = false;
        this->script_mode = false;
    }

    void interact(){
        stdin_fileno = dup(STDIN_FILENO);
        cout << start_message;

        while(!finish_interaction){
            string s;
            getline(cin, s);

            if(script_mode && s == "@") {
                script_mode = !script_mode;
                fflush(stdin);
                dup2(stdin_fileno, STDIN_FILENO);
                stdin_fileno = dup(STDIN_FILENO);
                //getline(cin, s);
            }
            else process_input(s);
        }
    }
};

//Interactor::Interactor(int obj_socket) = default;

#endif //TEST_INTERACTOR_H
