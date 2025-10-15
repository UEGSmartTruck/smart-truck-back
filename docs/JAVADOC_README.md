# Publicar JavaDoc

Este documento descreve como gerar e publicar a documentação Javadoc do projeto.

Gerar a documentação localmente:

1. Execute:

   mvn -DskipTests javadoc:javadoc

2. A saída ficará em `target/site/apidocs`.

Publicar manualmente (usando o script):

1. Gere o javadoc conforme acima.
2. Execute:

   ./scripts/publish-javadoc.sh gh-pages

   O script criará um worktree no branch `gh-pages` e fará push das páginas.

Publicar via CI (GitHub Actions):

O workflow `.github/workflows/javadoc.yml` gera o javadoc e publica automaticamente
para o branch `gh-pages` usando a ação `peaceiris/actions-gh-pages` quando houver
push no branch `main`.

Notas:
- Se preferir, pode usar `ghp-import` (Python) ou outras ferramentas para publicar.
- Certifique-se de que o branch `gh-pages` existe ou permita que a ação o crie.
