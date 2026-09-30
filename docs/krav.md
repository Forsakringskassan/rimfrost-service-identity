# Krav: Mock tjänst för Identity API

## Bakgrund: 

Implementera en provider för Identity API (definierat i `rimfrost-service-identity-openapi`) som returnerar
identitet baserat på värden inkluderade i bearer token. Syftet är att ha en fungerande stub att testa mot innan riktig integration
finns på plats.

## Funktionella krav

### IDENT-FR-01 - Hämta Identitet

- **IDENT-FR-01.1** `GET /identity` ska returnera identitet tillsammans med HTTP 200 status om en authorization header med giltigt bearer token i formatet `<idtyp>:<varde>` inkluderas med förfrågan.
- **IDENT-FR-01.2** `GET /identity` ska returnera HTTP 401 status om `Authorization` header inte är inkluderad med förfrågan.
- **IDENT-FR-01.3** `GET /identity` ska returnera HTTP 401 status om `Authorization` header inte är av typen `Bearer Token authentication`.
- **IDENT-FR-01.4** `GET /identity` ska returnera HTTP 401 status om bearer token inte är i formatet `<idtyp>:<varde>`.
- **IDENT-FR-01.5** `GET /identity` ska returnera HTTP 401 status om id typ eller värde är tomt i bearer token.  