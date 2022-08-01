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
update todo set completed_at = null where no = 6;
-- delete from todo where no= 5;
-- rollback;
select * from todo;

-- 할일 오름차순, 완료목록 내림차순
select * from (select * from todo where completed_at is null order by no)
union all
select * from (select * from todo where completed_at is not null order by no desc)
;


-- board 테이블
create table board(
    no number,
    title varchar2(2000),
    member_id varchar2(15),
    content varchar2(4000),
    created_at date default sysdate,
    updated_at date default sysdate,
    read_count number default 0,
    constraint pk_board_no primary key(no),
    constraint fk_board_member_id foreign key(member_id) references member(member_id) on delete set null
);

create sequence seq_board_no;
drop sequence seq_board_no;

create table attachment(
    no number,
    board_no number not null,
    original_filename varchar2(256) not null,
    renamed_filename varchar2(256) not null,
    download_count number default 0,
    created_at date default sysdate,
    constraint pk_attachment_no primary key(no),
    constraint fk_attachment_board_no foreign key(board_no) references board(no) on delete cascade
);

create sequence seq_attachment_no;


-- sample data
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 1','abcde','반갑습니다',to_date('18/02/10','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 2','qwerty','안녕하세요',to_date('18/02/12','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 3','admin','반갑습니다',to_date('18/02/13','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 4','abcde','안녕하세요',to_date('18/02/14','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 5','qwerty','반갑습니다',to_date('18/02/15','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 6','admin','안녕하세요',to_date('18/02/16','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 7','abcde','반갑습니다',to_date('18/02/17','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 8','qwerty','안녕하세요',to_date('18/02/18','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 9','admin','반갑습니다',to_date('18/02/19','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 10','abcde','안녕하세',to_date('18/02/20','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 11','qwerty','반갑습니다',to_date('18/03/11','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 12','admin','안녕하세',to_date('18/03/12','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 13','abcde','반갑습니다',to_date('18/03/13','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 14','qwerty','안녕하세',to_date('18/03/14','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 15','admin','반갑습니다',to_date('18/03/15','RR/MM/DD'),0);

Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 16','abcde','안녕하세',to_date('18/03/16','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 17','qwerty','반갑습니다',to_date('18/03/17','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 18','admin','안녕하세',to_date('18/03/18','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 19','abcde','반갑습니다',to_date('18/03/19','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 20','qwerty','안녕하세',to_date('18/03/20','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 21','admin','반갑습니다',to_date('18/04/01','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 22','abcde','안녕하세',to_date('18/04/02','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 23','qwerty','반갑습니다',to_date('18/04/03','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 24','admin','안녕하세',to_date('18/04/04','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 25','abcde','반갑습니다',to_date('18/04/05','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 26','qwerty','안녕하세',to_date('18/04/06','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 27','admin','반갑습니다',to_date('18/04/07','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 28','abcde','안녕하세',to_date('18/04/08','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 29','qwerty','반갑습니다',to_date('18/04/09','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 30','admin','안녕하세',to_date('18/04/10','RR/MM/DD'),0);

Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 31','abcde','반갑습니다',to_date('18/04/16','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 32','qwerty','안녕하세',to_date('18/04/17','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 33','admin','반갑습니다',to_date('18/04/18','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 34','abcde','안녕하세',to_date('18/04/19','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 35','qwerty','반갑습니다',to_date('18/04/20','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 36','admin','안녕하세',to_date('18/05/01','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 37','abcde','반갑습니다',to_date('18/05/02','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 38','qwerty','안녕하세',to_date('18/05/03','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 39','admin','반갑습니다',to_date('18/05/04','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 40','abcde','안녕하세',to_date('18/05/05','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 41','qwerty','반갑습니다',to_date('18/05/06','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 42','admin','안녕하세',to_date('18/05/07','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 43','abcde','반갑습니다',to_date('18/05/08','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 44','qwerty','안녕하세',to_date('18/05/09','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 45','admin','반갑습니다',to_date('18/05/10','RR/MM/DD'),0);

Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 46','abcde','안녕하세',to_date('18/05/16','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 47','qwerty','반갑습니다',to_date('18/05/17','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 48','admin','안녕하세',to_date('18/05/18','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 49','abcde','반갑습니다',to_date('18/05/19','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 50','qwerty','안녕하세',to_date('18/05/20','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 51','admin','반갑습니다',to_date('18/05/01','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 52','abcde','안녕하세',to_date('18/06/02','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 53','qwerty','반갑습니다',to_date('18/06/03','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 54','admin','안녕하세',to_date('18/06/04','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 55','abcde','반갑습니다',to_date('18/06/05','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 56','qwerty','안녕하세',to_date('18/06/06','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 57','admin','반갑습니다',to_date('18/06/07','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 58','abcde','안녕하세',to_date('18/06/08','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 59','qwerty','반갑습니다',to_date('18/06/09','RR/MM/DD'),0);
Insert into SPRING.BOARD (NO,TITLE,MEMBER_ID,CONTENT,CREATED_AT,READ_COUNT) values (SEQ_BOARD_NO.nextval,'안녕하세요, 게시판입니다 - 60','admin','안녕하세',to_date('18/06/10','RR/MM/DD'),0);

--  첨부파일 테이블 샘플데이터
INSERT INTO ATTACHMENT VALUES(SEQ_ATTACHMENT_NO.NEXTVAL, 60, 'test.jpg', '20200525_090909_123.JPG', DEFAULT, DEFAULT);
INSERT INTO ATTACHMENT VALUES(SEQ_ATTACHMENT_NO.NEXTVAL, 60, 'moon.jpg', '20200525_090909_345.JPG', DEFAULT, DEFAULT);
INSERT INTO ATTACHMENT VALUES(SEQ_ATTACHMENT_NO.NEXTVAL, 59, 'sun.jpg', '20200525_020425_345.JPG', DEFAULT, DEFAULT);

commit;

select * from board;
select * from attachment;

-- paging query
-- cPage, numPerPage
select *
from
    (select
        row_number() over(order by no desc) rnum,
        b.*
    from
        board b)
where rnum between 1 and 5;

-- rowbounds 객체 이용시
select * from board order by no desc;
select * from attachment;


-- board - member - attachment
select *
from
    board b
        left join member m on b.member_id = m.member_id
        left join attachment a on b.no = a.board_no
where b.no = 103
order by b.no desc;



-- spring-security관련 authority 테이블 생성
create table authority(
    member_id varchar2(20),
    auth varchar2(50),
    constraint pk_authority primary key(member_id, auth),
    constraint fk_authority_member_id foreign key(member_id) references member(member_id) on delete cascade
);

insert into authority values ('abcde', 'ROLE_USER');
insert into authority values ('qwerty', 'ROLE_USER');
insert into authority values ('admin', 'ROLE_USER');
insert into authority values ('admin', 'ROLE_ADMIN');
insert into authority values ('honggd', 'ROLE_USER');
insert into authority values ('sinsa', 'ROLE_USER');

commit;

select * from authority;
select * from member;

select *
from member m join authority a on m.member_id = a.member_id
where m.member_id = 'admin';


-- bcrypt 비밀번호 수정
update member set password = '$2a$10$.eu3p.QmAQtq08IJPZuAJOacnGuqPvk6mTrC1gR8cXY8hNDUBru6q' where password = '1234';


-- remember-me테이블
create table persistent_logins (
    username varchar(64) not null, 
    series varchar(64) primary key, 
    token varchar(64) not null, --username, password, expiry time을 hashing한 값
    last_used timestamp not null
);

select * from persistent_logins;


-- rest api
-- 메뉴기능

create table menu (
    id number,
    restaurant varchar2(512) not null,
    name varchar2(256) not null,
    price number,
    type varchar2(10) not null, --kr, ch, jp
    taste varchar2(10) not null, -- mild, hot
    constraint pk_menu_id primary key(id),
    constraint uq_menu unique(restaurant, name, taste)
);
create sequence seq_menu_id;

insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'두리순대국','순대국',7000,'kr','mild');
insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'두리순대국','순대국',7000,'kr','hot');
insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'장터','뚝배기 김치찌게',7000,'kr','hot');
insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'만리향','간짜장',5000,'ch','mild');
insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'만리향','짬뽕',6000,'ch','hot');
insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'짬뽕지존','짬뽕',9000,'ch','mild');
insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'김남완초밥집','완초밥',13000,'jp','mild');
insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'김남완초밥집','런치초밥',10000,'jp','mild');
insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'김남완초밥집','참치도로초밥',33000,'jp','mild');
insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'진가와','자루소면',8000,'jp','mild');
insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'진가와','자루소바',9000,'jp','mild');
insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'백운봉','막국수',9000,'kr','hot');
insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'대우부대찌게','부대지게',10000,'kr','hot');
insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'봉된장','열무비빔밥+우렁된장',7000,'kr','hot');
insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'대우부대찌게','부대찌게',10000,'kr','hot');
insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'산들애','딸기',500,'kr','hot');
insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'대우부대찌게','청국장',13000,'kr','mild');
insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'스타동','사케동',8400,'jp','mild');
insert into spring.menu (id,restaurant,name,price,type,taste) values (seq_menu_id.nextval,'진씨화로','돌솥비빔밥',7000,'kr','mild');

commit;

select 
    *
from
    menu;



-- 채팅 기능구현
create table chat_member(
    chatroom_id varchar2(50),
    member_id varchar2(50),
    last_check number default 0, -- 유닉스초로 관리
    created_at date default sysdate,
    deleted_at date, -- 채팅방 나간 시간
    constraint pk_chat_member primary key(chatroom_id, member_id)
);

create table chat_log (
    no number,
    chatroom_id varchar2(50),
    member_id varchar2(50),
    msg varchar2(4000),
    time number,
    constraint pk_chat_log_no primary key(no),
    constraint fk_chat_log foreign key(chatroom_id, member_id) references chat_member(chatroom_id, member_id)
);

create sequence seq_chat_log_no;

select * from chat_member;
select * from chat_log order by time;

insert into chat_log values (seq_chat_log_no.nextval,'S658069H', 'admin', '안녕하세요', '1658974834858');
insert into chat_log values (seq_chat_log_no.nextval,'S658069H', 'admin', '왜 웃으세요?', '1658975436728');
insert into chat_log values (seq_chat_log_no.nextval,'fy9l3094', 'admin', '뭔가요?', '1658977563525');
insert into chat_log values (seq_chat_log_no.nextval,'Gc3L597M', 'admin', '김사랑씨인걸로 아는데', '1658977677720');
commit;

-- 관리자 채팅목록 조회
select
    no, 
    chatroom_id, 
    (select member_id from chat_member where chatroom_id = cl.chatroom_id and member_id != 'admin') member_id, 
    msg, 
    time,
    (select count(*)from chat_log where chatroom_id = cl.chatroom_id and time > (select last_check from chat_member where chatroom_id = cl.chatroom_id and member_id = 'admin')) unread_count
from
    ( select 
        cl.*, 
        row_number() over(partition by chatroom_id order by time desc) rnum 
    from chat_log cl) cl 
where rnum = 1;


select count(*)
from chat_log
where chatroom_id = 'S658069H'
    and
        time > (select last_check from chat_member where chatroom_id = 'S658069H' and member_id = 'admin');

delete from chat_log where member_id = 'qwerty';
commit;

select
    no, 
    chatroom_id, 
    (select member_id from chat_member where chatroom_id = cl.chatroom_id and member_id != 'admin') member_id, 
    msg, 
    time,
    (select count(*)from chat_log where chatroom_id = cl.chatroom_id and time > (select last_check from chat_member where chatroom_id = cl.chatroom_id and member_id = 'honggd')) unread_count
from
    ( select 
        cl.*, 
        row_number() over(partition by chatroom_id order by time desc) rnum 
    from chat_log cl) cl 
where rnum = 1;


select
    no, 
    chatroom_id, 
    (select member_id from chat_member where chatroom_id = cl.chatroom_id and member_id != 'admin') member_id, 
    msg, 
    time,
    (select count(*) from chat_log where chatroom_id = cl.chatroom_id and time > (select last_check from chat_member where chatroom_id = cl.chatroom_id and member_id = 'honggd')) unread_count
from
    ( select 
        cl.*, 
        row_number() over(partition by chatroom_id order by time desc) rnum 
    from chat_log cl) cl
where rnum = 1;

select count(*) 
from chat_log cl 
where 
    chatroom_id = (select chatroom_id from chat_member where member_id = 'love')
    and time > (select last_check from chat_member where member_id = 'love') 
    and member_id = 'admin';

select count(*) from chat_log where chatroom_id = '' and time > (select last_check from chat_member where chatroom_id = '' and member_id = '');