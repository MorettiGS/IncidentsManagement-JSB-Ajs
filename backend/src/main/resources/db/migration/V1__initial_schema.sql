CREATE TABLE IF NOT EXISTS app_user (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id UUID NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS incident (
    id UUID PRIMARY KEY,
    titulo VARCHAR(120) NOT NULL,
    descricao TEXT,
    prioridade VARCHAR(10) NOT NULL CHECK (prioridade IN ('BAIXA','MEDIA','ALTA')),
    status VARCHAR(15) NOT NULL CHECK (status IN ('ABERTA','EM_ANDAMENTO','RESOLVIDA','CANCELADA')),
    responsavel_email VARCHAR(255) NOT NULL,
    tags TEXT,
    data_abertura TIMESTAMP NOT NULL,
    data_atualizacao TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS comment (
    id UUID PRIMARY KEY,
    incident_id UUID NOT NULL,
    autor VARCHAR(120) NOT NULL,
    mensagem TEXT NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    FOREIGN KEY (incident_id) REFERENCES incident(id) ON DELETE CASCADE
);

CREATE INDEX idx_incident_status ON incident(status);
CREATE INDEX idx_incident_prioridade ON incident(prioridade);
CREATE INDEX idx_incident_responsavel_email ON incident(responsavel_email);
CREATE INDEX idx_comment_incident_id ON comment(incident_id);

CREATE INDEX idx_incident_titulo ON incident(titulo);
CREATE INDEX idx_incident_descricao ON incident(descricao);
