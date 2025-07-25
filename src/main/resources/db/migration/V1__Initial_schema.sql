-- Create initial schema for User Manager Microservice

-- Users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    keycloak_id VARCHAR(100) UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    last_login_at TIMESTAMP,
    login_attempts INTEGER NOT NULL DEFAULT 0,
    locked_until TIMESTAMP,
    profile_picture_url VARCHAR(1000),
    bio VARCHAR(500),
    department VARCHAR(100),
    position VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Roles table
CREATE TABLE roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(500),
    system_role BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Permissions table
CREATE TABLE permissions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    type VARCHAR(20) NOT NULL,
    resource VARCHAR(100) NOT NULL,
    action VARCHAR(50) NOT NULL,
    system_permission BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Menus table
CREATE TABLE menus (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    url VARCHAR(255),
    icon VARCHAR(100),
    display_order INTEGER NOT NULL DEFAULT 0,
    visible BOOLEAN NOT NULL DEFAULT TRUE,
    parent_id UUID REFERENCES menus(id),
    required_permission VARCHAR(100),
    menu_level INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Screens table
CREATE TABLE screens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    type VARCHAR(20) NOT NULL,
    module VARCHAR(100) NOT NULL,
    route VARCHAR(255),
    component VARCHAR(255),
    public_access BOOLEAN NOT NULL DEFAULT FALSE,
    auth_required BOOLEAN NOT NULL DEFAULT TRUE,
    cache_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    cache_duration INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Papers table
CREATE TABLE papers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(100) NOT NULL UNIQUE,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    category VARCHAR(100),
    file_path VARCHAR(500),
    file_name VARCHAR(255),
    file_size BIGINT,
    mime_type VARCHAR(100),
    version_number INTEGER NOT NULL DEFAULT 1,
    is_latest_version BOOLEAN NOT NULL DEFAULT TRUE,
    parent_paper_id VARCHAR(100),
    published_at TIMESTAMP,
    expires_at TIMESTAMP,
    created_by UUID NOT NULL REFERENCES users(id),
    approved_by UUID REFERENCES users(id),
    download_count BIGINT NOT NULL DEFAULT 0,
    view_count BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- User Profiles table
CREATE TABLE user_profiles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id),
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    type VARCHAR(20) NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    is_public BOOLEAN NOT NULL DEFAULT FALSE,
    context VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Many-to-many relationship tables

-- User-Roles relationship
CREATE TABLE user_roles (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- Role-Permissions relationship
CREATE TABLE role_permissions (
    role_id UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id UUID NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

-- Screen Permissions (ElementCollection)
CREATE TABLE screen_permissions (
    screen_id UUID NOT NULL REFERENCES screens(id) ON DELETE CASCADE,
    permission_code VARCHAR(100) NOT NULL,
    PRIMARY KEY (screen_id, permission_code)
);

-- Paper Permissions (ElementCollection)
CREATE TABLE paper_permissions (
    paper_id UUID NOT NULL REFERENCES papers(id) ON DELETE CASCADE,
    permission_code VARCHAR(100) NOT NULL,
    PRIMARY KEY (paper_id, permission_code)
);

-- Paper Tags (ElementCollection)
CREATE TABLE paper_tags (
    paper_id UUID NOT NULL REFERENCES papers(id) ON DELETE CASCADE,
    tag VARCHAR(50) NOT NULL,
    PRIMARY KEY (paper_id, tag)
);

-- User Profile Attributes (ElementCollection)
CREATE TABLE user_profile_attributes (
    profile_id UUID NOT NULL REFERENCES user_profiles(id) ON DELETE CASCADE,
    attribute_key VARCHAR(100) NOT NULL,
    attribute_value VARCHAR(1000),
    PRIMARY KEY (profile_id, attribute_key)
);

-- User Profile Permissions (ElementCollection)
CREATE TABLE user_profile_permissions (
    profile_id UUID NOT NULL REFERENCES user_profiles(id) ON DELETE CASCADE,
    permission_code VARCHAR(100) NOT NULL,
    PRIMARY KEY (profile_id, permission_code)
);

-- User Profile Preferences (ElementCollection)
CREATE TABLE user_profile_preferences (
    profile_id UUID NOT NULL REFERENCES user_profiles(id) ON DELETE CASCADE,
    preference_key VARCHAR(100) NOT NULL,
    preference_value VARCHAR(500),
    PRIMARY KEY (profile_id, preference_key)
);

-- Create indexes for better performance

-- User indexes
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_user_username ON users(username);
CREATE INDEX idx_user_keycloak_id ON users(keycloak_id);
CREATE INDEX idx_user_status ON users(status);
CREATE INDEX idx_user_created_at ON users(created_at);

-- Role indexes
CREATE INDEX idx_role_name ON roles(name);
CREATE INDEX idx_role_code ON roles(code);

-- Permission indexes
CREATE INDEX idx_permission_code ON permissions(code);
CREATE INDEX idx_permission_type ON permissions(type);
CREATE INDEX idx_permission_resource ON permissions(resource);

-- Menu indexes
CREATE INDEX idx_menu_code ON menus(code);
CREATE INDEX idx_menu_parent_id ON menus(parent_id);
CREATE INDEX idx_menu_order ON menus(display_order);

-- Screen indexes
CREATE INDEX idx_screen_code ON screens(code);
CREATE INDEX idx_screen_type ON screens(type);
CREATE INDEX idx_screen_module ON screens(module);

-- Paper indexes
CREATE INDEX idx_paper_code ON papers(code);
CREATE INDEX idx_paper_type ON papers(type);
CREATE INDEX idx_paper_status ON papers(status);
CREATE INDEX idx_paper_category ON papers(category);
CREATE INDEX idx_paper_created_by ON papers(created_by);

-- User Profile indexes
CREATE INDEX idx_user_profile_user_id ON user_profiles(user_id);
CREATE INDEX idx_user_profile_type ON user_profiles(type);
CREATE INDEX idx_user_profile_name ON user_profiles(name);

-- Relationship table indexes
CREATE INDEX idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX idx_user_roles_role_id ON user_roles(role_id);
CREATE INDEX idx_role_permissions_role_id ON role_permissions(role_id);
CREATE INDEX idx_role_permissions_permission_id ON role_permissions(permission_id);
CREATE INDEX idx_screen_permissions_screen_id ON screen_permissions(screen_id);
CREATE INDEX idx_paper_permissions_paper_id ON paper_permissions(paper_id);
CREATE INDEX idx_paper_tags_paper_id ON paper_tags(paper_id);
CREATE INDEX idx_user_profile_attributes_profile_id ON user_profile_attributes(profile_id);
CREATE INDEX idx_user_profile_permissions_profile_id ON user_profile_permissions(profile_id);
CREATE INDEX idx_user_profile_preferences_profile_id ON user_profile_preferences(profile_id);