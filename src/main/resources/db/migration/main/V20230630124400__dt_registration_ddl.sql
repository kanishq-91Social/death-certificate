CREATE TABLE public.eg_dt_registration
(
    id character varying(64)  NOT NULL,
    registrationno character varying(64)  NOT NULL,
    hospitalname character varying(500) ,
    dateofreport timestamp without time zone,
    dateofdeath timestamp without time zone NOT NULL,
    firstname character varying(200) ,
    middlename character varying(200) ,
    lastname character varying(200) ,
    placeofdeath character varying(1000) ,
    informantsname character varying(200) ,
    informantsaddress character varying(1000) ,
    createdtime bigint,
    createdby character varying(64) ,
    lastmodifiedtime bigint,
    lastmodifiedby character varying(64) ,
    counter smallint,
    tenantid character varying(50)  NOT NULL,
    gender smallint NOT NULL,
    remarks character varying(1000) ,
    aadharno character varying(150) ,
    nationality character varying(100) ,
    religion character varying(100) ,
    icdcode character varying(300) ,
    age character varying(100) ,
    CONSTRAINT eg_death_dtls_pkey PRIMARY KEY (id),
    CONSTRAINT eg_death_dtls_ukey1 UNIQUE (registrationno, tenantid)
);

CREATE TABLE public.eg_dt_address
(
    id character varying(64)  NOT NULL,
    buildingno character varying(1000) ,
    houseno character varying(1000) ,
    streetname character varying(1000) ,
    locality character varying(1000) ,
    tehsil character varying(1000) ,
    district character varying(100) ,
    city character varying(100) ,
    state character varying(100) ,
    pinno character varying(100) ,
    country character varying(100) ,
    createdby character varying(64) ,
    createdtime bigint,
    lastmodifiedby character varying(64) ,
    lastmodifiedtime bigint,
    deathdtlid character varying(64) ,
    CONSTRAINT eg_death_permaddr_pkey PRIMARY KEY (id),
    CONSTRAINT eg_death_permaddr_fkey1 FOREIGN KEY (deathdtlid)
        REFERENCES public.eg_dt_registration (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE eg_wf2_processinstance(

    id character varying(64),
    tenantid character varying(128),
    businessService character varying(128),
    businessId character varying(128),
    action character varying(128),
    status character varying(128),
    comment character varying(128),
    assigner character varying(128),
    assignee character varying(128),
    sla bigint,
    previousStatus character varying(128),
    createdBy character varying(64),
    lastModifiedBy character varying(64),
    createdTime bigint,
    lastModifiedTime bigint
);