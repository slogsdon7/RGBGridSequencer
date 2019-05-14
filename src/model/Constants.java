package model;

class Constants {

    static String create_schema = "\n" +
            "    create table if not exists project\n" +
            "(\n" +
            "    name        text not null,\n" +
            "    id          INTEGER\n" +
            "        primary key,\n" +
            "    description text\n" +
            ");\n" +
            "\n" +
            "create table if not exists sequence\n" +
            "(\n" +
            "    id         INTEGER\n" +
            "        primary key,\n" +
            "    project_id int\n" +
            "        constraint sequence_project_id_fk\n" +
            "            references project\n" +
            "            on delete cascade\n" +
            ");\n" +
            "\n" +
            "create table if not exists step\n" +
            "(\n" +
            "    id          INTEGER\n" +
            "        primary key,\n" +
            "    position    int not null,\n" +
            "    sequence_id int not null\n" +
            "        constraint step_sequence_id_fk\n" +
            "            references sequence\n" +
            "            on delete cascade\n" +
            ");\n" +
            "\n" +
            "create table if not exists pad\n" +
            "(\n" +
            "    pos      int,\n" +
            "    id       INTEGER\n" +
            "        primary key,\n" +
            "    color    int,\n" +
            "    step_id  int\n" +
            "        constraint pad_step_id_fk\n" +
            "            references step\n" +
            "            on delete cascade,\n" +
            "    flashing boolean default false,\n" +
            "    pulsing  boolean default false\n" +
            ");\n" +
            "\n" +
            "create unique index if not exists pad_step_id_index_uindex\n" +
            "    on pad (step_id, pos);\n" +
            "\n";
}
