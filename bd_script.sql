CREATE DATABASE libreria;
USE libreria;

create table tperfil(
	id_perfil int auto_increment not null primary key,
    desc_perfil varchar(100)
);

create table tcliente(
	id_cli int auto_increment not null primary key,
    nom_cli varchar(60),
    apell_cli varchar(60),
    telf_cli int,
    dni_cli int,
    direc_cli varchar(100),
    email_cli varchar(60)
);

create table telf_cli(
	id_telcli int auto_increment not null primary key,
    servicio varchar(60),
    id_cli int,
    foreign key (id_cli) references tcliente(id_cli)
);

--------------------------------------------------------------

create table tusuario(
	id_usu int auto_increment not null primary key,
    nom_usu varchar(60),
    ape_usu varchar(60),
    pass_usu varchar(60),
    dni_usu bigint,
    telf_usu int,
    direc_usu varchar(100),
    fec_ingreso_usu date,
    id_perfil int,
    foreign key (id_perfil) references tperfil(id_perfil)
);

create table tconfiguracion(
	id_config int auto_increment not null primary key,
    nom_empresa varchar(60),
    dir_empresa varchar(100),
    telf_empresa int,
    ruc_empresa bigint,
    nom_responsable_empresa varchar(100),
    igv_empresa double,
    cambio double,
    anuncio_empresa varchar(200),
    id_usu int,
    foreign key (id_usu) references tusuario(id_usu)
);

create table tasistencia(
	id_as int auto_increment not null primary key,
    hora_as time,
    fecha_as date,
    tipo_as varchar(20),
    min_tarde_as double,
    faltas_as int,
    obs_as varchar(100),
    id_usu int,
    foreign key (id_usu) references tusuario(id_usu)
);

create table tgastos(
	idgasto int auto_increment not null primary key,
    concepto varchar(200),
    monto double
);


-----------------------------------------------------------

create table tproveedor(
	id_provee int auto_increment not null primary key,
    nom_provee varchar(100),
    ruc_provee bigint,
    dir_provee varchar(100),
    telf_provee int,
    ciudad_provee varchar(60),
    contacto_provee varchar(60),
    email_provee varchar(100),
    web_provee varchar(100)
);

create table telf_prov(
	id_telpro int auto_increment not null primary key,
    servicio varchar(60),
    id_provee int,
    foreign key (id_provee) references tproveedor(id_provee)
);


create table tmarca(
	id_marca int auto_increment not null primary key,
    nom_marca varchar(100),
    id_provee int,
    foreign key (id_provee) references tproveedor(id_provee)
);

create table tproductos(
	id_prod int auto_increment not null primary key,
    nom_prod varchar(100),
    stock_prod int,
    fecha_ingreso_prod date,
    prec_uni_prod double,
    id_marca int,
    foreign key (id_marca) references tmarca(id_marca)
);

create table ttipodoc(
	idtipo int auto_increment not null primary key,
    descripcion varchar(100)
);

create table tcompras(
	id_compra int auto_increment not null primary key,
    num_doc int,
    forma_pago varchar(60),
    fecha_compra date,
    hora_compra time,
    idtipo int,
    foreign key (idtipo) references ttipodoc(idtipo)
);

create table tdetallecompra(
	id_compra int,
    id_prod int,
    cantidad int,
    precio double,
    subtotal double,
    foreign key (id_compra) references tcompras(id_compra),
    foreign key (id_prod) references tproductos(id_prod)
);


create table tventas(
	id_venta int auto_increment not null primary key,
    fecha_venta date,
    hora_venta time,
    id_usu int,
    id_cli int,
    foreign key (id_usu) references tusuario(id_usu),
    foreign key (id_cli) references tcliente(id_cli)
);

create table tdetalleventa(
	id_venta int,
    id_prod int,
    cantidad int,
    precio double,
    subtotal double,
    foreign key (id_venta) references tventas(id_venta),
    foreign key (id_prod) references tproductos(id_prod)
);


create table tboleta(
	id_boleta int auto_increment not null primary key,
    fecha_boleta date,
    id_venta int,
    foreign key (id_venta) references tdetalleventa(id_venta)
);

