#!/usr/bin/env bash
# Publica a documentação Javadoc gerada localmente para o branch gh-pages.
# Uso: ./scripts/publish-javadoc.sh [branch]
# Exemplo: ./scripts/publish-javadoc.sh gh-pages

set -euo pipefail

BRANCH=${1:-gh-pages}
ROOT_DIR=$(git rev-parse --show-toplevel)
TARGET_DIR="${ROOT_DIR}/target/site/apidocs"

if [ ! -d "$TARGET_DIR" ]; then
  echo "Javadoc não encontrado em $TARGET_DIR. Gere com: mvn -DskipTests javadoc:javadoc"
  exit 1
fi

echo "Publicando Javadoc para branch '$BRANCH'..."

# criar um worktree temporário para o branch de páginas
TMP_DIR=$(mktemp -d)
# Se o branch não existir, git worktree add fará fallback para criar a branch localmente
if ! git worktree add -B "$BRANCH" "$TMP_DIR" origin/$BRANCH 2>/dev/null; then
  git worktree add -B "$BRANCH" "$TMP_DIR"
fi

rm -rf "$TMP_DIR"/*
cp -r "$TARGET_DIR"/* "$TMP_DIR"/

pushd "$TMP_DIR" > /dev/null
git add --all
git commit -m "Update javadoc: $(date -u +%Y-%m-%dT%H:%M:%SZ)" || echo "No changes to commit"
git push origin "$BRANCH"
popd > /dev/null

git worktree remove "$TMP_DIR" --force
rm -rf "$TMP_DIR"

echo "Publicação concluída."
