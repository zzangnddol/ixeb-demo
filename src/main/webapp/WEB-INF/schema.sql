create table COM_CODE
(
	STDCODE VARCHAR2(30) not null,
	GRPCODE VARCHAR2(3) not null,
	CODNAME VARCHAR2(50),
	DESCRIPTION VARCHAR2(255),
	PRT_SEQ NUMBER,
	USEYN VARCHAR2(1),
	constraint COM_CODEPK
		primary key (STDCODE, GRPCODE)
)
/

create table USERFILE
(
	USER_ID VARCHAR2(10) not null
		constraint USERFILEPK
			primary key,
	CST_CLS VARCHAR2(255),
	CST_NM VARCHAR2(30),
	CST_NBR VARCHAR2(10),
	EMAIL_ADDR VARCHAR2(60),
	TEAM_NM VARCHAR2(30),
	USER_NM VARCHAR2(30),
	USER_PW VARCHAR2(16),
	GROUP_ID VARCHAR2(10)
		constraint FK8LF6X5L6XPI01WXFKBKH4DJVU
			references USERFILE,
	CELL_PHONE VARCHAR2(13)
)
/

create table WEBMENU
(
	MENU_ID VARCHAR2(10) not null
		constraint WEBMENUPK
			primary key,
	MENU_BM VARCHAR2(50),
	MENU_TYPE VARCHAR2(1),
	PRT_SEQ VARCHAR2(4),
	USEYN VARCHAR2(1),
	UP_MENU_ID VARCHAR2(10)
		constraint FKQ2BPPAD0WW40QBR86Q0ATL9QV
			references WEBMENU
)
/

create table WEBAUTH
(
	MENU_ID VARCHAR2(255) not null
		constraint FKKM2N2RBANTS84S02IQLW3BLU6
			references WEBMENU,
	USER_ID VARCHAR2(255) not null
		constraint FKEU595GCNSJS8QJ49P18LOM1TH
			references USERFILE,
	constraint WEBAUTHPK
		primary key (MENU_ID, USER_ID)
)
/

create table TBL_DATA_SLICE
(
	ROW_NUMBER NUMBER,
	FIELD1 VARCHAR2(255),
	FIELD2 VARCHAR2(255),
	FIELD3 VARCHAR2(255),
	FIELD4 VARCHAR2(255),
	FIELD5 VARCHAR2(255),
	FIELD6 VARCHAR2(255),
	FIELD7 VARCHAR2(255),
	FIELD8 VARCHAR2(255),
	FIELD9 VARCHAR2(255),
	FIELD10 VARCHAR2(255)
)
/

create table TBL_SERVICE04
(
	ID VARCHAR2(255),
	ORG VARCHAR2(255),
	NAME VARCHAR2(255),
	TEL VARCHAR2(255),
	NOTE VARCHAR2(255)
)
/

create table TBL_PMS001
(
	COMPID VARCHAR2(100),
	NAME VARCHAR2(100),
	PARENT_ID VARCHAR2(100),
	ID VARCHAR2(100),
	LEVLCD NUMBER,
	TITEXT1 NUMBER,
	TITEXT2 NUMBER,
	TITEXT3 NUMBER,
	TITEXT4 NUMBER,
	TITEXT5 NUMBER,
	TITEXT6 NUMBER,
	TITEXT7 NUMBER,
	TITEXT8 NUMBER,
	TITEXT9 NUMBER,
	TITEXT10 NUMBER,
	TITEXT11 NUMBER,
	TITEXT12 NUMBER,
	TITEXT13 NUMBER,
	TITEXT14 NUMBER,
	TITEXT15 NUMBER,
	TITEXT16 NUMBER,
	TITEXT17 NUMBER,
	TITEXT18 NUMBER,
	TITEXT19 NUMBER,
	TITEXT20 NUMBER,
	TITEXT21 NUMBER,
	TITEXT22 NUMBER,
	TITEXT23 NUMBER,
	TITEXT24 NUMBER,
	TITEXT25 NUMBER,
	TITEXT26 NUMBER,
	TITEXT27 NUMBER,
	TITEXT30 NUMBER,
	FULLTIME_TOTAL NUMBER,
	TOTAL NUMBER,
	SORT VARCHAR2(100),
	SEQ VARCHAR2(100)
)
/

create table TBL_CCTV
(
	AGENCY VARCHAR2(1000),
	ADDRESSNEW VARCHAR2(1000),
	ADDRESSOLD VARCHAR2(1000),
	PURPOSE VARCHAR2(1000),
	NUMBER1 NUMBER,
	PIXELS NUMBER,
	SHOOTINGSCENE VARCHAR2(1000),
	DAYOFKEEP NUMBER,
	DATEOFINSTALLATION VARCHAR2(1000),
	AGENCYPHONENUMBER VARCHAR2(1000),
	LATITUDE NUMBER(28,10),
	LONGITUDE NUMBER(28,10),
	DATEBASED VARCHAR2(1000)
)
/

create table TBL_NOTICE
(
	SEQ VARCHAR2(500),
	TITLE VARCHAR2(500),
	DATEREG VARCHAR2(500),
	CONTENT VARCHAR2(500)
)
/

create table TBL_MENU
(
	ID VARCHAR2(255),
	TEXT VARCHAR2(255),
	ICON VARCHAR2(255),
	URL VARCHAR2(255),
	SORT VARCHAR2(255)
)
/

INSERT INTO HAN.USERFILE (USER_ID, CST_CLS, CST_NM, CST_NBR, EMAIL_ADDR, TEAM_NM, USER_NM, USER_PW, GROUP_ID, CELL_PHONE) VALUES ('admin', null, '운영', null, 'admin@admin.com', '운영팀', '운영자', 'adminPass', null, null);