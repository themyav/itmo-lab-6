#include <bits/stdc++.h>
#include <sys/socket.h>
#include <arpa/inet.h>

#include <unistd.h>
#include <cstring>
#include <fcntl.h>

#include "libs/command.pb.h"
#include "libs/command_list.h"
#include "libs/Interactor.h"
#include <fstream>
#include "google/protobuf/arena.h"

#define PORT 8080

using namespace std;

//10004 вариант

int main ()
{

    setlocale(LC_ALL, "Russian");
    int obj_socket = socket (AF_INET, SOCK_STREAM, 0 );
    //cout << flush;
    //fcntl(obj_socket, F_SETFL, O_NONBLOCK);

    if (obj_socket < 0){
        cout << "Ошибка при создании сокета!" << endl;
        return -1;
    }
    sockaddr_in serv_addr{};
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(PORT);

    // Крнвертирую IPv4 и IPv6 адреса из текста в бинарную форму
    if(inet_pton ( AF_INET, "127.0.0.1", &serv_addr.sin_addr) <= 0){
        cout << "Некорректный адрес! Данный ip адрес не поддерживается." << endl;
        return -1;
    }


    if ( connect( obj_socket, (struct sockaddr *)&serv_addr, sizeof(serv_addr )) < 0){
        cout <<"Соединение разорвано: невозможно установить соединение с сервером!" << endl;
        return -1;
    }

    Interactor interactor(obj_socket);
    interactor.interact();
    return 0;

}
//execute_script /home/myav/CLionProjects/test/resource/input.txt
