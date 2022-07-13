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


-- member 테이블 생성
create table member(
    member_id varchar2(15),
    password varchar2(300) not null,
    name varchar2(256) not null,
    gender char(1),
    birthday date,
    email varchar2(256),
    phone char(11) not null,
    address varchar2(512),
    hobby varchar2(256),
    created_at date default sysdate,
    updated_at date,
    enabled number default 1, -- 활성화여부 1 사용중, 0 비활성화
    constraint pk_member_id primary key(member_id),
    constraint ck_member_gender check(gender in('M', 'F')),
    constraint ck_member_enabled check(enabled in(1, 0))
);

insert into spring.member values ('abcde','1234','아무개','M',to_date('88-01-25','rr-mm-dd'),'abcde@naver.com','01012345678','서울시 강남구','운동,등산,독서',default, null, default);
insert into spring.member values ('qwerty','1234','김말년','F',to_date('78-02-25','rr-mm-dd'),'qwerty@naver.com','01098765432','서울시 관악구','운동,등산',default, null, default);
insert into spring.member values ('admin','1234','관리자','F',to_date('90-12-25','rr-mm-dd'),'admin@naver.com','01012345678','서울시 강남구','독서',default, null, default);
commit;
-- delete from member where member_id = 'sinsa';

select * from member;
desc member;

-- todo 테이블 생성
create table todo (
    no number,
    todo varchar2(4000),
    created_at date default sysdate,
    completed_at date,
    constraint pk_todo_no primary key(no)
);

create sequence seq_todo_no;

insert into todo values(seq_todo_no.nextval, '우산 정리하기', default, default);
insert into todo values(seq_todo_no.nextval, '형광등 교체', default, default);
insert into todo values(seq_todo_no.nextval, '장보기', default, default);
insert into todo values(seq_todo_no.nextval, '에어컨 필터 청소', default, default);
commit;

-- 할일 완료
update todo set completed_at = sysdate where no = 6;
-- delete from todo where no= 5;

select * from todo;

-- 할일 오름차순, 완료목록 내림차순
select * from (select * from todo where completed_at is null order by no)
union all
select * from (select * from todo where completed_at is not null order by no desc)
;