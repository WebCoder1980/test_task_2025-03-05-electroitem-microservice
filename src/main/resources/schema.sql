CREATE TABLE IF NOT EXISTS counter (
	"name" varchar(75) NOT NULL,
	currentid int8 NOT NULL,
	CONSTRAINT counter_pkey PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS electro_item_type (
    id_ int8 NOT NULL,
    name varchar(150) NOT NULL,
    CONSTRAINT electro_item_type_pkey PRIMARY KEY (id_)
);

CREATE TABLE IF NOT EXISTS electro_item (
    id_ int8 NOT NULL,
    name varchar(150) NOT NULL,
    typeId int8 NOT NULL,
    price numeric(10, 2) NOT NULL,
    quantity int4 NOT NULL,
    archive bool NOT NULL,
    description text,
    CONSTRAINT electro_item_pkey PRIMARY KEY (id_),
    CONSTRAINT fk_electro_item_type FOREIGN KEY (typeId) REFERENCES electro_item_type(id_)
);