CREATE TABLE workspace_invites (
    id UUID PRIMARY KEY,
    workspace_id UUID NOT NULL REFERENCES workspaces(id) ON DELETE CASCADE,
    email VARCHAR(255) NOT NULL,
    role VARCHAR(16) NOT NULL,
    token VARCHAR(64) NOT NULL UNIQUE,
    expires_at TIMESTAMPTZ NOT NULL,
    accepted_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX workspace_invites_workspace_id_idx ON workspace_invites(workspace_id);
CREATE INDEX workspace_invites_token_idx ON workspace_invites(token);
