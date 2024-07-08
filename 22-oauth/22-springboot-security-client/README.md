# Backend cliente de nuestro servidor de autorizaciones

Este es nuestro cliente backend.

# Testing

Levantamos primero nuestro servidor de autorizaciones y luego este cliente backend.

Vamos a la ruta siguiente para hacer el login:

http://127.0.0.1:8080/oauth2/authorization/client-app

Como nombre de usuario indicamos `pepe` y como password `12345`.

Esto nos devuelve el código de autorización.

Ejemplo: 

```
{
"code": "i2APrjBvp1LeANbKq7DEbYE2vVKEEuW9dhjYGZiECOsLuq6w14GezvNZsoU6h0IwZCPUhf-wTS-vvfQuQjEoi1605yQs-T_tcO_Q4ucsZrAB1jwZLB6mU2wk3XX_SirY"
}
```

Nos vamos a Postman para generar el token usando ese código de autorización. Esto hay que hacerlo medianamente rápido para que el code no expire.

Seleccionamos `POST` y la ruta `http://127.0.0.1:9000/oauth2/token`.

Vamos a `Authorization` y seleccionamos del desplegable `Basic Auth`.

Indicamos en Username el usuario del cliente `client-app` y en Password `12345`.

Vamos a `Body`, seleccionamos `form-data`.

Escribimos en Key `code` y en Value (puede variar) `i2APrjBvp1LeANbKq7DEbYE2vVKEEuW9dhjYGZiECOsLuq6w14GezvNZsoU6h0IwZCPUhf-wTS-vvfQuQjEoi1605yQs-T_tcO_Q4ucsZrAB1jwZLB6mU2wk3XX_SirY`

Escribimos en Key `grant_type` y en Value `authorization_code`

Escribimos en Key `redirect_uri` y en Value `http://127.0.0.1:8080/authorized`

Pulsamos botón `Send` y nos devuelve:

```
{
    "access_token": "eyJraWQiOiIwZDBmYjU2NS1jZGZhLTQ3ZDMtODdhMi1hZWJiYjYxZjFiZjciLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJwZXBlIiwiYXVkIjoiY2xpZW50LWFwcCIsIm5iZiI6MTcxMzA3NDEzMSwic2NvcGUiOlsicmVhZCIsIm9wZW5pZCIsInByb2ZpbGUiXSwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMTo5MDAwIiwiZXhwIjoxNzEzMDc0NDMxLCJpYXQiOjE3MTMwNzQxMzEsImp0aSI6ImY2MDIzYThjLTg1NDQtNGY4YS04OWMwLTJlNGY0MTkwYzU2OSJ9.M8-AJ8TE4YD-i-FNOg7dStMlB6FAHmC3D2GdKT9t84sEe_Ig4WCP0ZFYntJv2X8ujgwrY8ZneUTx4RoPw1G-mDwvvtxJxEDnuwif0pYNXouTNGopcx0y3aVNXyQYomnRqDUu3Z4oof0qINxKIw2uKvuiRBsNdCg78LKWTXjEoQgGgPdExMOtwhL9J3AFmXiIN2-fWIReRy2I9IoaWtR1j0i1CxhArB0AULK3qAEAeUUNUnImb3C_SQbmXtA_jUD4zkBOKmvnEL4jniolB2vc_1ILPz2h8Vo-rSeq1DJf154qzbyL3gEUNQEW4NfRGx40267A5KVjlH2e_BBrwNAL4A",
    "refresh_token": "3udyP_CsY2qwggd1E_p_-NegLAqH3JYXLnO5Ixo712sVIEeqFZL31RhCOEmSvyro4t5AVv-wuEcA5u9s2k525c6bhalYamWbbZkXLH6OUaV5BixVsX4WfgF0b8ASEAfH",
    "scope": "read openid profile",
    "id_token": "eyJraWQiOiIwZDBmYjU2NS1jZGZhLTQ3ZDMtODdhMi1hZWJiYjYxZjFiZjciLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJwZXBlIiwiYXVkIjoiY2xpZW50LWFwcCIsImF6cCI6ImNsaWVudC1hcHAiLCJhdXRoX3RpbWUiOjE3MTMwNzQxMTAsImlzcyI6Imh0dHA6Ly8xMjcuMC4wLjE6OTAwMCIsImV4cCI6MTcxMzA3NTkzMSwiaWF0IjoxNzEzMDc0MTMxLCJub25jZSI6IktGaFBta29EdTRVaVUwZEZlSDZNS0JoTWZCM3QzR1VaRE9tdTJYM2ZuSlUiLCJqdGkiOiJjZTEwYTcwOC04M2E5LTQxYWItYjQ0ZC0yMGMxYzI4ZTBjYTUiLCJzaWQiOiJGX25RTXlSMzgzUW9hVkVQdnJPaWIxUTJ1U2ZkQjhHa1VoSmZhaUU3WDNFIn0.QHmhp6fwVCHwYmancJ8kY6tCF3ugyr0BuY4KjHTDneVF-rf9xdQjMyd_nkF_QbM6Ey2gvEAtbmxucsIHPjFVVOw1kPbXVjQ3rPRiMLh2av8qrFVhND3Se_ULRQpi2ZjJkinP75Nea-oOauIHDSncHTsq7YvAIoxQB5nULfiMcuPN-Zelz9xMFfXRcrT6H_K6ZZ2YLjAkwYQJ0OBuF98wfcPaExtNgy6fEfix8V4xVU-iRN8jMxCxjLJLkeqhCTidn_ZECJKmzJlfheZYXEcklrl71FxmEfvncy-GwExXUa6H0Rek2fhJDubZOaTEki2OjzwnmRyPNIzWAvcg9Rk9Rw",
    "token_type": "Bearer",
    "expires_in": 299
}
```

NOTA: Si nos vamos a la página de JWT y pegamos ese access_token, veremos su payload:

```
{
  "sub": "pepe",
  "aud": "client-app",
  "nbf": 1713077443,
  "scope": [
    "read",
    "openid",
    "profile",
    "write"
  ],
  "iss": "http://127.0.0.1:9000",
  "exp": 1713077743,
  "iat": 1713077443,
  "jti": "f43608ff-9151-446a-a5c6-0b283d01c3a5"
}
```

Hacemos otra petición Postman. Es un `GET` a la ruta `http://127.0.0.1:8080/list`

Vamos a `Authorization` y seleccionamos del desplegable `Bearer Token` y en Token informamos el access_token generado antes.

Pulsamos el botón Send y nos devuelve:

```
[
    {
        "text": "Test list"
    }
]
```

Hacemos otra petición Postman. Es un `POST` a la ruta `http://127.0.0.1:8080/create`

Vamos a `Authorization` y seleccionamos del desplegable `Bearer Token` y en Token informamos el access_token generado antes.

Vamos a `Body`, seleccionamos `raw` y `JSON` e indicamos el siguiente JSON:

```
{
    "text": "hola que tal!"
}
```

Pulsamos el botón Send y nos devuelve:

```
{
    "text": "hola que tal!"
}
```

En la carpeta `Postman` aparece un archivo con los endpoints de Postman para importarlos y probar.