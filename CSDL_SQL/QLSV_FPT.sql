use master
go
CREATE DATABASE QLSV_FPT2
GO
USE QLSV_FPT2
GO

CREATE TABLE STUDENTS
(
	MASV int not null primary key,
	Hoten nvarchar(50) null,
	Email nvarchar(50) null,
	SoDT nvarchar(15) null,
	GioiTinh bit null,
	DiaChi nvarchar(500) null,
	Hinh nvarchar(50) null
)
CREATE TABLE GRADE
(
	ID int IDENTITY(1,1) primary key,
	MASV int NOT NULL,
	Tienganh int null,
	Tinhoc int null,
	GDTC INT NULL,
)
CREATE TABLE USERS
(
	usernames nvarchar(50) not null,
	passwords nvarchar(50)null,
	roles nvarchar(50) null
)


ALTER TABLE dbo.USERS ADD CONSTRAINT PK_khoa2 primary key(usernames);
ALTER TABLE dbo.GRADE ADD CONSTRAINT FK_key FOREIGN KEY(MASV)REFERENCES dbo.STUDENTS (MASV);
go
insert into USERS
values  (N'DUYAN2','123',N'Giáo viên'),
		(N'DUYAN','12345',N'Ðào tạo')
