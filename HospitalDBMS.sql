create table medication(
    med_name varchar(9),
    manufacturer varchar(15),
    descrpt varchar(50),
    primary key(med_name)
);
create table address(
    addressID varchar(9),
    addr varchar(15),
    city varchar(15),
    state varchar(15),
    zip varchar(5),
    primary key(addressID)
);
create table contact_info(
    contactID varchar(9),
    fname varchar(15),
    mname varchar(15),
    lname varchar(15),
    curr_address varchar(9),
    perm_address varchar(9),
    perm_phone varchar(11),
    curr_phone varchar(11),
    bdate date,
    sex char(1),
    primary key(contactID),
    foreign key(curr_address) references address(addressID),
    foreign key(perm_address) references address(addressID)    
);
create table doctor(
    ssn varchar(9) not null unique,
    dptment varchar(15),
    doc_ID varchar(8),
    contact varchar(9),
    primary key(doc_ID),
    foreign key(contact) references contact_info(contactID)
);
create table patient(
    p_ID varchar(9),
    ssn varchar(9) unique not null,
    pr_doctor varchar(8),
    sec_doctor varchar(8),
    contact varchar(9),
    condition varchar(10),
    primary key(p_ID),
    foreign key(pr_doctor) references doctor(doc_ID),
    foreign key(sec_doctor) references doctor(doc_ID),
    foreign key(contact) references contact_info(contactID)
);
create table prescription(
    doc_id varchar(8),
    pat_id varchar(9),
    med_name varchar(15),
    fecha date,
    primary key(doc_id, pat_id, med_name),
    foreign key(doc_id) references doctor(doc_ID),
    foreign key(pat_id) references patient(p_ID),
    foreign key(med_name) references medication(med_name)
);
create table department(
    dpt_code varchar(9),
    dpt_name varchar(20) unique not null,
    dpt_head varchar(8),
    office_number varchar(3),
    office_phone varchar(10),
    primary key(dpt_code),
    foreign key (dpt_head) references doctor(doc_id)
);
create table procedure(
    proc_number varchar(9),
    off_dptment varchar(9),
    pname varchar(9),
    descption varchar(50),
    duration int,
    primary key(proc_number),
    foreign key(off_dptment) references department(dpt_code)
);
create table interaction(
    int_id varchar(8),
    pat_id varchar(9) not null,
    datetime timestamp,
    descrption varchar(50),
    primary key(int_id),
    foreign key(pat_id) references patient(p_ID)
);
create table proc_record(
    p_number varchar(9),
    int_id varchar(9),
    doc_id varchar(9),
    fecha date,
    hora date,
    notes varchar(30),
    primary key(p_number, int_id, doc_id),
    foreign key(p_number) references procedure(proc_number),
    foreign key(int_id) references interaction(int_id),
    foreign key(doc_id) references doctor(doc_id)
);

alter table doctor add foreign key(dptment) references department(dpt_code);



