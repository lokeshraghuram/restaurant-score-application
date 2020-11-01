create table inspection.restaurant_status (
	business_id varchar(255) not null,
	inspection_date timestamp with time zone,
	status varchar(255),
	PRIMARY KEY (business_id)
);

create table inspection.restaurant_data (
        business_id varchar(255) not null,
        business_name varchar(255),
		business_address varchar(255),
        business_city varchar(255),
        business_state varchar(255),
		business_postal_code varchar(255),
		business_latitude varchar(255),
        business_longitude varchar(255),
        business_location point,
        business_phone_number varchar(255),
        inspection_id varchar(255),
		inspection_date timestamp with time zone,
		inspection_score decimal,
		inspection_type varchar(255),
		violation_id varchar(255),
		violation_description varchar(255),
		risk_category varchar(255),
        CONSTRAINT PK_business PRIMARY KEY (business_id,inspection_id,violation_id)
        /* Uncomment below two lines if data setup is done in restaurant_status table. For casual testing of the application, I commented foreign key relationship */
        /*,*/
		/*CONSTRAINT fk_business FOREIGN KEY(business_id) REFERENCES inspection.restaurant_status(business_id)*/
);

	
