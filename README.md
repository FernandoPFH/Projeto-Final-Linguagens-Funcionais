# Projeto-Final-Linguagens-Funcionais

## Como Rodar:

    docker-compose up --build

## Portas Abertas:

- 8080 => Adminer
- 8000 => API

## Rotas API:

```/ :get```

### Retorno:

    "Hello World!" str

```/usuarios :get```

Retorna Todos Os Usuários.

### Retorno:

    {
        status: str,
        usuarios [
            {
                id: int,
                nome: str
            }
        ]
    }

### Exemplo

    {
        status: "Ok",
        usuarios: [
            {
                id: 1,
                nome: "Fernando"
            }
        ]
    }

```/criar-usuario :post```

Adicionar Novo Usuário.

### Requisição:

    {
        nome: str
    }

### Exemplo:

    {
        nome: "Fernando"
    }

### Retorno:

    {
        status: str,
        usuario_id: int
    }

### Exemplo

    {
        status: "Ok",
        usuario_id: 1
    }

```/usuarios/:id :get```

Retorna Um Usuário.

### Retorno:

    {
        status: str,
        usuario: {
            {
                id: int,
                nome: str
            }
        }
    }

### Exemplo

    {
        status: "Ok",
        usuario: {
            {
                id: 1,
                nome: "Fernando"
            }
        }
    }

```/atualizar-usuario/:id :put```

Atualizar Usuário.

### Requisição:

    {
        nome: str
    }

### Exemplo:

    {
        nome: "Fernando"
    }

### Retorno:

    {
        status: str,
        usuario_id: int
    }

### Exemplo

    {
        status: "Ok",
        usuario_id: 1
    }

```/deletar-usuario/:id :delete```

Deletar Usuário.

### Retorno:

    {
        status: str,
        usuario_id: int
    }

### Exemplo

    {
        status: "Ok",
        usuario_id: 1
    }