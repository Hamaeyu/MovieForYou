-- 오라클 12c버전 이상은 GENERATED ALWAYS AS IDENTITY 자동 증가 컬럼 방식이 있지만
-- 시퀀스 + 트리거 전통적인 방식을 사용함
-- 설계 속성 : 테이블 생성-> 시퀀스 생성 -> 트리거 생성

-- 테이블 생성 DDL
-- pk, not null, check만 바로 걸고
-- fk 제약조건은 테이블 생성 순서를 맞춰야하니까 alter문으로 나중에 적용
create table movie_temp(
    id          number      constraint  movie_temp_id_pk    primary key,
    movie_code  varchar2(50 char) not null
        constraint movie_temp_code_uk unique,
    json_row    clob    not null,
    status      varchar2(10 char) not null
        constraint movie_temp_status_chk check (status in ('NEW', 'PARSED', 'ERROR', 'SKIPPED')),
    created_at   timestamp    default systimestamp  not null,
    updated_at   timestamp -- null허용, update이벤트 발생 시 트리거로 자동 갱신
);

-- 국가(마스터 테이블)
create table country(
    id              number(3)   constraint  country_id_pk   primary key,
    country_name    varchar2(50 char)   not null
        constraint  country_name_uk unique
);

-- 장르(마스터 테이블)
create table genre(
  id            number(2)   constraint genre_id_pk  primary key,
  genre_name    varchar2(30 char)  not null
    constraint genre_name_uk unique
);

-- 배우(마스터 테이블)
create table actor(
    id  number(10)  constraint actor_id_pk  primary key,
    actor_name  varchar2(100 char) not null
    -- 동명이인 있을 수 있으므로 uk는 걸지 않음
    -- role_type varchar2(20 char)  -- 주연/조연 등 역할 유형 추가하면 좋을 것 같음
);

-- 다 : 다 해소 테이블
create table movie_actor( -- nn 일부러 가독성 위해서 명시함
    movie_id    number(10)  not null,
    actor_id    number(10)  not null,
    constraint movie_actor_pk primary key (movie_id, actor_id)
);

--영화 테이블
    -- *참고: 감독/장르는 실제로는 다대다 관계이나, 
    --  포트폴리오 개발 기간(8일) 고려하여 단순화함
create table movie(
    id              number(10)  constraint movie_id_pk  primary key,
    movie_title     varchar2(300 char)  not null,
    director        varchar2(100 char)  not null, 
    -- 감독 여러명 일 수 있으나, 테이블로 또 빼기에는 포트폴리오용으로 너무 복잡해져서 생략함
    rating          varchar2(5 char)    not null,
    release_date    date    not null,
    runtime         number(5)   not null,
    synopsis    varchar2(2000 char), --데이터 파싱 안될 수 있으므로 null허용함
    trailer_url varchar2(1000 char), --데이터 파싱 안될 수 있으므로 null허용함
    country_id  number(3)   not null, --fk alter문으로 나중에 걸음
    genre_id    number(2)   not null --fk alter문으로 나중에 걸음
    --장르 여러개 일 수도 있어서.. 다대다로 빼야 하지만.. 대표 장르 1개 관리 (복잡도 고려)
);

--OST 테이블
create table ost(
    id  number(10)  constraint  ost_id_pk   primary key,
    ost_title   varchar2(300 char)  not null,
    ost_path    varchar2(1000), --데이터 파싱 안될 수 있으므로 null허용
    ost_singer  varchar2(200 char) not null, -- 가수 여러명 있을 수 있으나(단순화)
    movie_id    number(10) not null  -- fk alter문으로 걸음
    -- 원래는 1:다 관계이나, 짧은 개발기간으로 인해 단순화함
);

--리뷰 댓글 테이블
create table review_comment(
    id  number  constraint  review_comm_id_pk   primary key,
    star_rating number(1)   default 0  not null
        constraint review_comm_star_chk check (star_rating between 0 and 5),
    review_content  varchar2(2000 char) not null,
    created_at timestamp default systimestamp not null,
    updated_at timestamp, --null허용, 트리거로 자동 관리
    is_deleted char(1) default 'N' -- 논리 삭제함
        constraint review_comm_is_del_chk check (is_deleted in ('Y','N')),
    depth   number(1)   default 0 not null -- 0,1,2로 3단계로 제한함
        constraint  review_comm_depth_chk check(depth between 0 and 2),
    root_id number,  -- 최상위 댓글 번호(조회/그룹핑용) 자기자신일 수도 있어서 null허용
    -- 중복 가능함, fk 안걸음(불필요)
    parent_id number, --fk 자신이 부모이면 null(alter문으로 적용)
    user_id number(10) not null, --fk alter문으로 적용
    movie_id number(10) not null --fk alter문으로 적용
);

-- 포토 테이블
create table photo(
    id  number  constraint  photo_id_pk primary key,
    photo_path  varchar2(2000)  not null,
    photo_category_id   number(2)   not null, -- fk
    movie_id    number(10)  not null -- fk
);

--포토 카테고리(마스터 테이블)
create table photo_category(
    id  number(2) constraint  photo_cat_id_pk  primary key,
    category_type    varchar2(10 char)   not null
        constraint photo_cat_type_uk  unique
);

-- 권한 테이블(마스터 테이블)
create table role(
    id          number(1)   constraint role_id_pk primary key,
    role_name   varchar2(10 char)   not null
        constraint  role_name_uk    unique
);

-- 회원 권한 테이블(다 : 다 해소)
create table user_role( -- 가독성 위해 not null 명시
    user_id number(10)  not null, --fk alter문
    role_id number(1)   not null, --fk alter문
    constraint user_role_pk primary key(user_id, role_id) -- 복합 PK 설정
);

-- 회원 테이블(user가 키워드라서 app_user사용)
-- 이메일은 대소문자 구분 없이 UNIQUE 보장 위해 lower(email) 인덱스 추가
create table app_user(
    id      number(10)   constraint  app_user_id_pk  primary key,
    email   varchar2(255 char) not null,
        -- 따로 인덱스 생성으로 unique 적용함
    password_hash   varchar2(255)   not null,
    nickname    varchar2(30 char)   not null
        constraint  app_user_nickname_uk unique,
    is_deleted  char(1) default 'N' not null
        constraint  app_user_is_del_chk check(is_deleted in ('Y', 'N')),
    created_at  timestamp    default systimestamp not null
);

--게시글 타입 테이블(마스터 테이블)
create table post_type(
    id  number(2) constraint  post_type_id_pk   primary key,
    type_name   varchar2(20 char)   not null
        constraint  post_type_name_uk   unique
);

--게시글 테이블(공통, 부모)
create table post(
    id  number  constraint  post_id_pk  primary key,
    post_title  varchar2(100 char)  not null,
    post_content    clob    not null,
    created_at  timestamp  default systimestamp   not null,
    updated_at  timestamp, --트리거로 자동 관리
    user_id     number(10)  not null, --fk
    type_id     number(2)   not null --fk
);

--댓글 테이블
--comment는 예약어(테이블명으로 사용 불가)
create table post_comment(
    id  number  constraint  comment_id_pk   primary key,
    comment_content varchar2(2000 char) not null,
    created_at  timestamp    default systimestamp not null,
    updated_at  timestamp,  --트리거로 자동 관리
    post_id     number  not null --fk
);

--영화 관람 후기 테이블(자식, 구체화)
create table review_post(
    id  number  constraint  review_post_id_pk   primary key,
    -- id는 식별관계(FK가 PK), fk는 alter문으로 적용
    short_review    varchar2(100 char) not null,
    star_rating     number(1)   not null
        constraint  review_post_star_chk   check(star_rating between 0 and 5)
);

--상영관(+좌석)평가 게시판 테이블(자식, 구체화)
create table cinema_post(
    id  number  constraint  cinema_post_id_pk   primary key,
    -- id는 식별관계(FK가 PK), fk는 alter문으로 적용
    overall_review  varchar2(255 char), -- 총평
    cinema_rating   number(1)   default 0   not null
        constraint  cinema_rating_chk check(cinema_rating between 0 and 5),
    screen_number   varchar2(10 char),--몇 관에서 봤는지
    screen_type     varchar2(10 char) 
        constraint screen_type_chk check (screen_type in ('2D', '3D', 'IMAX', '4DX')),
    seat_row    varchar2(1 char) not null, --좌석행
    seat_col    varchar2(2 char) not null, --좌석 열번호
    seat_rating number(1)   default 0   not null --좌석 평점
        constraint  seat_rating_chk check(seat_rating between 0 and 5),
    watch_time date, --관람시작시간
    cinema_id   number(5)    not null --fk
);

--영화관 마스터 테이블
create table cinema(
    id  number(5)   constraint  cinema_id_pk  primary key,
    cinema_name varchar2(20 char)   not null
        constraint  cinema_name_uk  unique,
    latitude    number(10,7), --위도
    longitude   number(10,7),--경도
    cinema_address  varchar2(300 char),
    region_id   number  not null, --fk
    cinema_type_id  number(1) not null --fk
);

--지역 마스터 테이블
create table region(
    id  number(3)   constraint  region_id_pk  primary key,
    region_name     varchar2(10 char)   not null
        constraint  region_name_uk  unique
);

--영화관 브랜드
create table cinema_brand(
    id  number(1)   constraint  cinema_brand_id_pk  primary key,
    brand_name  varchar2(10 char)   not null 
        constraint cinema_brand_name_uk unique
);

--------------------------------------------------------------------------
-- fk 적용
-- 영화 테이블 FK
alter table movie
    add constraint movie_country_id_fk
    foreign key (country_id) references country(id);
    
alter table movie
    add constraint movie_genre_id_fk
    foreign key (genre_id) references genre(id);

-- 영화-배우 매핑 테이블 FK
alter table movie_actor
    add constraint movie_actor_movie_id_fk
    foreign key (movie_id) references movie(id);

alter table movie_actor
    add constraint movie_actor_actor_id_fk
    foreign key (actor_id) references actor(id);

-- OST 테이블 FK
alter table ost
    add constraint ost_movie_id_fk
    foreign key (movie_id) references movie(id);

-- 리뷰 댓글 테이블 FK
alter table review_comment
    add constraint review_comment_user_id_fk
    foreign key (user_id) references app_user(id);

alter table review_comment
    add constraint review_comment_movie_id_fk
    foreign key (movie_id) references movie(id);

alter table review_comment
    add constraint review_comment_parent_id_fk
    foreign key (parent_id) references review_comment(id);

-- 포토 테이블 FK
alter table photo
    add constraint photo_movie_id_fk
    foreign key (movie_id) references movie(id);

alter table photo
    add constraint photo_category_id_fk
    foreign key (photo_category_id) references photo_category(id);

-- 회원 권한 테이블 FK
alter table user_role
    add constraint user_role_user_id_fk
    foreign key (user_id) references app_user(id);

alter table user_role
    add constraint user_role_role_id_fk
    foreign key (role_id) references role(id);

-- 게시글 테이블 FK
alter table post
    add constraint post_user_id_fk
    foreign key (user_id) references app_user(id);

alter table post
    add constraint post_type_id_fk
    foreign key (type_id) references post_type(id);

-- 댓글 테이블 FK
alter table post_comment
    add constraint post_comment_post_id_fk
    foreign key (post_id) references post(id);

-- 영화관람후기 게시판 FK (post 자식)
alter table review_post
    add constraint review_post_post_id_fk
    foreign key (id) references post(id);

-- 상영관+좌석 평가 게시판 FK (post 자식)
alter table cinema_post
    add constraint cinema_post_post_id_fk
    foreign key (id) references post(id);

alter table cinema_post
    add constraint cinema_post_cinema_id_fk
    foreign key (cinema_id) references cinema(id);

-- 영화관 테이블 FK
alter table cinema
    add constraint cinema_region_id_fk
    foreign key (region_id) references region(id);

alter table cinema
    add constraint cinema_type_id_fk
    foreign key (cinema_type_id) references cinema_brand(id);


    
    
--------------------------------------------------------------------------
-- 테이블/컬럼 주석
-- 영화 임시 저장 테이블
comment on table movie_temp is 'Open API 데이터 파싱용 임시 저장 테이블';
comment on column movie_temp.id is '임시 데이터 번호(PK)';
comment on column movie_temp.movie_code is '영화 코드(API 고유 값)';
comment on column movie_temp.json_row is 'API 응답 JSON 데이터';
comment on column movie_temp.status is '처리 상태(NEW|PARSED|ERROR|SKIPPED)';
comment on column movie_temp.created_at is '데이터 수집 일시(기본값으로 관리)';
comment on column movie_temp.updated_at is '마지막 처리 일시(트리거로 자동 갱신)';

-- 마스터 테이블 - 한 번 구축 후 관리
-- 국가 테이블
comment on table country is '영화 제작 국가 관리(마스터 테이블 - Open API기반)';
comment on column country.id is '국가 번호(PK)';
comment on column country.country_name is '국가 이름';

-- 장르 테이블
comment on table genre is '영화 장르 관리(마스터 테이블 - Open API기반)';
comment on column genre.id is '장르 번호(PK)';
comment on column genre.genre_name is '장르명';

--배우 테이블
comment on table actor is '배우 데이터 관리(마스터 테이블 - Open API 기반)';
comment on column actor.id is '배우 번호(PK)';
comment on column actor.actor_name is '배우 이름';

--영화배우 테이블(다대다 해소 테이블)
comment on table movie_actor is '영화(movie_temp)와 배우(actor)의 다대다 관계 매핑 테이블';
comment on column movie_actor.movie_id IS '참조할 영화 ID (복합 PK, FK: movie_temp.id)';
comment on column movie_actor.actor_id IS '참조할 배우 ID (복합 PK, FK: actor.id)';

-- 영화 테이블
comment on table movie is '영화테이블 - Open API 응답 기반 데이터 저장';
comment on column movie.id is '영화 번호(PK)';
comment on column movie.movie_title is '영화 제목';
comment on column movie.director is '영화 감독';
comment on column movie.rating is '상영 등급 - 전세계 기준(테이블로 따로 빼면 좋지만 복잡도 고려하여 단순화)';
comment on column movie.release_date is '개봉일';
comment on column movie.runtime is '런타임';
comment on column movie.synopsis is '영화 줄거리';
comment on column movie.trailer_url is '영화 예고편 url';
comment on column movie.country_id is '제작국가번호(FK: country.id)';
comment on column movie.genre_id is '장르 번호(FK: genre.id)';

-- OST 테이블
comment on table ost is 'ost 테이블';
comment on column ost.ost_title is 'ost title명';
comment on column ost.ost_path is 'ost 경로';
comment on column ost.ost_singer is 'ost 가수';
comment on column ost.movie_id is '영화 번호(FK: movie.id)';

--리뷰 댓글 테이블
comment on table review_comment is '리뷰 댓글 테이블 - 영화 상세 페이지에 들어감';
comment on column review_comment.id is '리뷰 댓글 번호(PK)';
comment on column review_comment.star_rating is '영화 별점(0~5)';
comment on column review_comment.review_content is '댓글 내용(2000자 까지)';
comment on column review_comment.created_at is '작성일자(기본값 sysdate)';
comment on column review_comment.updated_at is '수정일자(트리거로 자동갱신)';
comment on column review_comment.is_deleted is '논리 삭제 여부(Y/N) - 기본값 N';
comment on column review_comment.depth is '깊이(UI 표현에 쓰임, 0~2로 제한)';
comment on column review_comment.root_id is '최상위 댓글 ID(조회/그룹핑 목적)';
comment on column review_comment.parent_id is '부모 댓글 ID(FK : self)';
comment on column review_comment.user_id is '회원ID(FK : app_user.id)';
comment on column review_comment.movie_id is '영화ID(FK : movie.id)';

-- 포토 테이블
comment on table photo is '포토 테이블 - 영화 이미지 데이터 관리용';
comment on column photo.id is '포토 번호(PK)';
comment on column photo.photo_path is '포토 이미지 경로';
comment on column photo.photo_category_id is '포토 카테고리번호(FK : photo_category.id)';
comment on column photo.movie_id is '영화 번호(FK : movie.id)';

-- 포토카테고리 테이블(마스터 테이블)
comment on table photo_category is '영화 포토 카테고리 마스터 테이블 - 이미지 분류 기준';
comment on column photo_category.id is '포토카테고리번호(PK)';
comment on column photo_category.category_type is '포토 구분 타입';

-- 권한 테이블(마스터 테이블)
comment on table role is '사용자 권한 마스터 테이블';
comment on column role.id is '권한 번호(PK)';
comment on column role.role_name is '권한명';

-- 회원 권한 테이블
comment on table user_role is '회원(app_user)과 권한(role)의 다대다 관계 매핑 테이블';
comment on column user_role.user_id IS '참조할 회원 ID (복합 PK, FK: users.id)';
comment on column user_role.role_id IS '참조할 권한 ID (복합 PK, FK: role.id)';

--회원 테이블
comment on table app_user is '회원 테이블';
comment on column app_user.id is '회원 번호(PK)';
comment on column app_user.email is '회원 이메일(로그인 겸용, unique index 적용)';
comment on column app_user.password_hash is '암호화된 비밀번호';
comment on column app_user.nickname is '회원 닉네임 (unique)';
comment on column app_user.is_deleted is '탈퇴 여부 (Y: 탈퇴, N: 활성) - 기본값 N';
comment on column app_user.created_at is '계정 생성 일시(기본값 - sysdate)';

--게시글 타입 테이블
comment on table post_type is '게시글 타입 마스터 테이블';
comment on column post_type.id is '게시글 타입 번호(PK)';
comment on column post_type.type_name is '타입명';

--게시글 테이블
comment on table post is '게시글 테이블 - 공통, 부모 테이블';
comment on column post.id is '게시글 번호(PK)';
comment on column post.post_title is '글 제목';
comment on column post.post_content is '글 내용';
comment on column post.created_at is '작성일자(기본값 - sysdate)';
comment on column post.updated_at is '수정일자(트리거로 자동관리)';
comment on column post.user_id is '회원번호(FK : app_user.id)';
comment on column post.type_id is '게시글 타입 번호(FK : post_type.id)';

--댓글 테이블
comment on table post_comment is '게시판 댓글 테이블';
comment on column post_comment.id is '댓글 번호(PK)';
comment on column post_comment.comment_content is '댓글 내용';
comment on column post_comment.created_at is '작성일자(기본값 - sysdate)';
comment on column post_comment.updated_at is '수정일자(트리거로 자동 관리)';
comment on column post_comment.post_id is '게시글 번호(FK : app_user.id)';

--영화관람후기 게시판
comment on table review_post is '영화관람후기게시판- post 자식 테이블(구체화)';
comment on column review_post.id is '글 번호(식별 관계 PK, FK : post.id)';
comment on column review_post.short_review is '한줄평';
comment on column review_post.star_rating is '영화후기별점(0~5)';

-- -- 상영관 + 좌석 평가 게시판 테이블
comment on table cinema_post is '상영관 + 좌석 평가 게시판 테이블 - post 자식 테이블(구체화)';
comment on column cinema_post.id is '게시글 번호(식별 관계 PK, FK: post.id)';
comment on column cinema_post.overall_review is '상영관 총평';
comment on column cinema_post.cinema_rating is '상영관 평점(0~5)';
comment on column cinema_post.screen_number is '몇 관에서 관람했는지';
comment on column cinema_post.screen_type is '상영방식(2D,3D,IMAX,4DX)';
comment on column cinema_post.seat_row is '좌석 행';
comment on column cinema_post.seat_col is '좌석 열번호';
comment on column cinema_post.seat_rating is '좌석 평점(0~5)';
comment on column cinema_post.watch_time is '관람 시작 시간';
comment on column cinema_post.cinema_id is '상영관 번호(FK: cinema.id)';

-- 영화관 테이블(마스터)
comment on table cinema is '영화관 마스터 테이블';
comment on column cinema.id is '영화관 번호(PK)';
comment on column cinema.cinema_name is '영화관명';
comment on column cinema.latitude is '위도 - map api에 사용 목적';
comment on column cinema.longitude is '경도 - map api에 사용 목적';
comment on column cinema.cinema_address is '영화관 주소';
comment on column cinema.region_id is '지역 번호(FK: region.id)';
comment on column cinema.cinema_type_id is '상영관 타입 번호(FK: cinema_brand.id)';

-- 지역 테이블(마스터)
comment on table region is '지역 마스터 테이블';
comment on column region.id is '지역 번호(PK)';
comment on column region.region_name is '지역명';

--영화관 브랜드(마스터)
comment on table cinema_brand is '영화관 브랜드 마스터 테이블';
comment on column cinema_brand.id is '브랜드 번호(PK)';
comment on column cinema_brand.brand_name is '브랜드명';

-----------------------------------------------------------------------------
-- 시퀀스 생성(설계속성 id컬럼용)
-- 시퀀스 테이블명 + '_' + 컬럼명 + '_SEQ'
-- Open API로 가져온 데이터 임시 저장 테이블 시퀀스
create sequence movie_temp_id_seq
    start with 1 -- 처음 시작값
    increment by 1 -- 1씩 증가
    cache 100 -- 시퀀스 값을 매번 디스크에서 가져오면 성능 떨어짐
    -- 캐시를 사용해서 미리 몇 개 값을 메모리에 올려서 빠르게 처리
    -- 장점 : insert가 많으면 성능에 좋다
    -- 대량으로 insert할거라서 좀 크게 잡음
    -- 미리 100개 값을 메모리에 올려서 빠르게 처리
    nocycle; -- 시퀀스의 최대값에 도달했을 때 번호 다시 안씀

--국가 id 시퀀스
create sequence country_id_seq
    start with 1 
    increment by 1
    cache 5
    nocycle; 
    
--장르 id 시퀀스    
create sequence genre_id_seq
    start with 1 
    increment by 1
    cache 2
    nocycle; 

-- 배우 id 시퀀스
create sequence actor_id_seq
    start with 1 
    increment by 1
    cache 5
    nocycle; 
    
-- 영화 id 시퀀스
create sequence movie_id_seq
    start with 1 
    increment by 1
    cache 20
    nocycle;
    
-- ost id 시퀀스
create sequence ost_id_seq
    start with 1 
    increment by 1
    cache 5
    nocycle;
    
-- 리뷰 댓글 id 시퀀스
create sequence review_comment_id_seq
    start with 1 
    increment by 1
    cache 20
    nocycle;

-- 포토 id 시퀀스
create sequence photo_id_seq
    start with 1 
    increment by 1
    cache 2
    nocycle;

-- 포토카테고리 id 시퀀스
create sequence photo_cat_id_seq
    start with 1 
    increment by 1
    cache 2
    nocycle;

-- 권한 id 시퀀스
create sequence role_id_seq
    start with 1 
    increment by 1
    cache 2
    nocycle;

-- 회원 id 시퀀스
create sequence app_user_id_seq
    start with 1 
    increment by 1
    cache 20
    nocycle;

-- 게시글 타입 id 시퀀스
create sequence post_type_id_seq
    start with 1 
    increment by 1
    cache 2
    nocycle;

-- 게시글 id 시퀀스
create sequence post_id_seq
    start with 1 
    increment by 1
    cache 20
    nocycle;

-- 댓글 id 시퀀스
create sequence post_comment_id_seq
    start with 1 
    increment by 1
    cache 20
    nocycle;

-- 영화관 id 시퀀스
create sequence cinema_id_seq
    start with 1 
    increment by 1
    cache 5
    nocycle;

-- 지역 id 시퀀스
create sequence region_id_seq
    start with 1 
    increment by 1
    cache 2
    nocycle;

-- 영화관 브랜드 id 시퀀스
create sequence cinema_brand_id_seq
    start with 1 
    increment by 1
    cache 2
    nocycle;


------------------------------------------------------------------------------
-- ID 자동 채번 트리거(pk컬럼에 insert 시 id 자동 입력 되도록 트리거 생성)
-- bir : before insert row(insert전에 행단위로 적용되는 트리거)
-- 운영 환경 안전성을 위해 CREATE OR REPLACE 대신 명시적 DROP/CREATE 사용
-- 트리거 제거: DROP TRIGGER movie_temp_bir_trg;
create trigger movie_temp_bir_trg
before insert on movie_temp
for each row
begin
    if :new.id is null then --개발자가 insert 시 id를 수동으로 넣지는 않았는지 확인함
        :new.id := movie_temp_id_seq.nextval; -- 시퀀스 다음값으로 할당
    end if;
end;
/

create trigger actor_bir_trg
before insert on actor
for each row
begin
    if :new.id is null then
        :new.id := actor_id_seq.nextval;
    end if;
end;
/

create trigger country_bir_trg
before insert on country
for each row
begin
    if :new.id is null then
        :new.id := country_id_seq.nextval;
    end if;
end;
/

create trigger genre_bir_trg
before insert on genre
for each row
begin
    if :new.id is null then
        :new.id := genre_id_seq.nextval;
    end if;
end;
/

create trigger ost_bir_trg
before insert on ost
for each row
begin
    if :new.id is null then
        :new.id := ost_id_seq.nextval;
    end if;
end;
/

create trigger photo_bir_trg
before insert on photo
for each row
begin
    if :new.id is null then
        :new.id := photo_id_seq.nextval;
    end if;
end;
/

create trigger app_user_bir_trg
before insert on app_user
for each row
begin
    if :new.id is null then
        :new.id := app_user_id_seq.nextval;
    end if;
end;
/

create trigger post_type_bir_trg
before insert on post_type
for each row
begin
    if :new.id is null then
        :new.id := post_type_id_seq.nextval;
    end if;
end;
/

create trigger post_bir_trg
before insert on post
for each row
begin
    if :new.id is null then
        :new.id := post_id_seq.nextval;
    end if;
end;
/

create trigger post_comment_bir_trg
before insert on post_comment
for each row
begin
    if :new.id is null then
        :new.id := post_comment_id_seq.nextval;
    end if;
end;
/

create trigger cinema_bir_trg
before insert on cinema
for each row
begin
    if :new.id is null then
        :new.id := cinema_id_seq.nextval;
    end if;
end;
/

create trigger region_bir_trg
before insert on region
for each row
begin
    if :new.id is null then
        :new.id := region_id_seq.nextval;
    end if;
end;
/

create trigger cinema_brand_bir_trg
before insert on cinema_brand
for each row
begin
    if :new.id is null then
        :new.id := cinema_brand_id_seq.nextval;
    end if;
end;
/

create trigger review_comment_bir_trg
before insert on review_comment
for each row
begin
    if :new.id is null then
        :new.id := review_comment_id_seq.nextval;
    end if;
end;
/

-- updated_at 자동 갱신 트리거
-- 레코드 수정 시 update_at을 현재 시각으로 자동 업데이트
-- 트리거 제거 DROP TRIGGER movie_temp_upd_trg;
create trigger movie_temp_upd_trg --트리거 이름 지정
before update on movie_temp -- update 실행되기 전 동작
for each row -- 각 행마다 실행
begin
    :NEW.updated_at := systimestamp;
    -- :NEW 업데이트 후 값, updated_at 현재 DB시각을 저장함
end;
/

create trigger review_comment_upd_trg
before update on review_comment
for each row
begin
    :new.updated_at := systimestamp;
end;
/

create trigger post_upd_trg
before update on post
for each row
begin
    :new.updated_at := systimestamp;
end;
/

create trigger post_comment_upd_trg
before update on post_comment
for each row
begin
    :new.updated_at := systimestamp;
end;
/

-------------------------------------------------------------------------

-- 인덱스
-- app_user.email에 대해 대소문자 구분 없이 UNIQUE 적용
-- 모든 이메일을 소문자로 변환해서 비교
create unique index app_user_email_uk
on app_user (lower(email));

-- FK와 검색/조회가 많이 발생할 컬럼 위주로 인덱스 생성
-- movie
create index movie_country_idx on movie(country_id);
create index movie_genre_idx on movie(genre_id);

-- ost
create index ost_movie_idx on ost(movie_id);

-- review_comment
create index review_comment_user_idx on review_comment(user_id);
create index review_comment_movie_idx on review_comment(movie_id);
create index review_comment_parent_idx on review_comment(parent_id);

-- review_comment: root_id로 댓글 그룹 조회 시 성능 향상
create index review_comment_root_idx on review_comment(root_id);

-- photo
create index photo_movie_idx on photo(movie_id);
create index photo_category_idx on photo(photo_category_id);


-- post
create index post_user_idx on post(user_id);
create index post_type_idx on post(type_id);

-- comment
create index post_comment_post_idx on post_comment(post_id);

-- cinema_post
create index cinema_post_cinema_idx on cinema_post(cinema_id);

-- cinema
create index cinema_region_idx on cinema(region_id);
create index cinema_type_idx on cinema(cinema_type_id);

-- 게시글/댓글 대량 조회 시:
-- 최신 글 순으로 조회할 때 빠름(페이징 처리 시 성능 개선)
create index post_created_idx on post(created_at DESC);

--특정 영화 댓글 조회 시 빠름
--(WHERE movie_id = ? ORDER BY created_at DESC)
create index review_comment_created_idx 
on review_comment(movie_id, created_at DESC);

