CREATE TABLE IF NOT EXISTS `empresa` (
`id` BIGINT(20) NOT NULL ,
`cnpj` VARCHAR(255) NOT NULL,
`data_atualizacao` DATETIME NOT NULL,
`data_criacao` DATETIME NOT NULL,
`razao_social` VARCHAR(255) NOT NULL
) ENGINE=INNODB DEFAULT CHARSET=utf8;

ALTER TABLE `empresa`
ADD PRIMARY KEY (`id`);

ALTER TABLE `empresa`
MODIFY `id` BIGINT(20) NOT NULL AUTO_INCREMENT;

CREATE TABLE IF NOT EXISTS `funcionario` (
	id BIGINT(20) NOT NULL ​,
	nome VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL,
	senha VARCHAR(255) NOT NULL,
	cpf VARCHAR(255) NOT NULL,
	valor_hora DECIMAL(19,2) DEFAULT NULL,
	qtd_horas_trabalho_dia FLOAT DEFAULT NULL,
	qtd_horas_almoco FLOAT DEFAULT NULL,
	perfil VARCHAR(255) NOT NULL,
	data_criacao DATETIME NOT NULL,
	data_atualizacao DATETIME NOT NULL,
	empresa_id BIGINT(20) DEFAULT NULL
) ENGINE=INNODB DEFAULT​ CHARSET=utf8;

ALTER TABLE `funcionario`
ADD PRIMARY KEY (`id`);

ALTER TABLE `funcionario`
MODIFY `id` BIGINT(20) NOT NULL AUTO_INCREMENT;

CREATE TABLE IF NOT EXISTS `lancamento` (
	id BIGINT(20) NOT NULL ​,
	data_criacao DATETIME NOT NULL,
	data_atualizacao DATETIME NOT NULL,   
	descricao VARCHAR(255) DEFAULT NULL,
	localizacao VARCHAR(255) DEFAULT NULL,	
	tipo VARCHAR(255) NOT NULL,
	funcionario_id BIGINT(20) DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=utf8;

ALTER TABLE `lancamento`
ADD PRIMARY KEY ​ (`id`);

ALTER TABLE `lancamento`
MODIFY `id` BIGINT(20) NOT NULL AUTO_INCREMENT;


ALTER TABLE `funcionario`
ADD CONSTRAINT `fk_funcionario_empresa` FOREIGN KEY (`empresa_id`) REFERENCES `empresa` (`id`); 

alter table `lancamento`
add constraint `fk_lancamento_funcionario` foreign key (`funcionario_id`) references `funcionario` (`id`); 