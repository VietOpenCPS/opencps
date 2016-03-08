create table opencps_department (
	departmentId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	parentId LONG,
	name VARCHAR(75) null,
	description VARCHAR(75) null
);

create table staff_Staff (
	staffId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	departmentId LONG,
	fullName VARCHAR(75) null,
	birthDay DATE null,
	address VARCHAR(75) null,
	email VARCHAR(75) null,
	phoneNumber VARCHAR(75) null,
	position VARCHAR(75) null,
	description VARCHAR(75) null
);