--============================================
-- 관리자계정
--============================================
-- spring계정 생성

alter session set "_oracle_script" = true;

create user spring
identified by spring
default tablespace users;

alter user spring quota unlimited on users;

grant connect, resource to spring;
----------------------------------------------

--============================================
-- spring 계정
--============================================
create table dev (
    no number,
    name varchar2(50) not null,
    career number not null,
    email varchar2(200) not null,
    gender char(1),
    lang varchar2(100) not null, -- Java, C (1정규형위반상태로 관리)
    created_at date default sysdate,
    constraint pk_dev_no primary key(no),
    constraint ck_dev_gender check(gender in ('M', 'F'))
);

create sequence seq_dev_no;


select * from dev;