#!/bin/bash

# =================================================================
# Script de inicialización de LocalStack
# Crea el bucket S3 necesario para el microservicio
# =================================================================

set -e  # Salir si hay algún error

echo "=================================================="
echo "🚀 Inicializando LocalStack para ms-prospectos"
echo "=================================================="

# Esperar a que LocalStack esté completamente listo
echo "⏳ Esperando a que LocalStack esté listo..."
sleep 5

# Configurar AWS CLI para usar LocalStack
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_DEFAULT_REGION=us-east-1

# Crear bucket S3
BUCKET_NAME="nitro-prospectos-assets-dev"

echo ""
echo "📦 Creando bucket S3: $BUCKET_NAME"

# Verificar si el bucket ya existe
if awslocal s3 ls "s3://$BUCKET_NAME" 2>/dev/null; then
    echo "✅ Bucket '$BUCKET_NAME' ya existe"
else
    # Crear el bucket
    awslocal s3 mb "s3://$BUCKET_NAME"
    echo "✅ Bucket '$BUCKET_NAME' creado exitosamente"
fi

# Configurar política de acceso público (para desarrollo local)
echo ""
echo "🔓 Configurando política de acceso público..."

awslocal s3api put-bucket-policy \
    --bucket "$BUCKET_NAME" \
    --policy '{
        "Version": "2012-10-17",
        "Statement": [
            {
                "Sid": "PublicReadGetObject",
                "Effect": "Allow",
                "Principal": "*",
                "Action": "s3:GetObject",
                "Resource": "arn:aws:s3:::'"$BUCKET_NAME"'/*"
            }
        ]
    }'

echo "✅ Política configurada"

# Crear estructura de carpetas (prefijos) en el bucket
echo ""
echo "📁 Creando estructura de carpetas..."

# Prospectos
awslocal s3api put-object \
    --bucket "$BUCKET_NAME" \
    --key "prospectos/fotos/" \
    --content-length 0

awslocal s3api put-object \
    --bucket "$BUCKET_NAME" \
    --key "prospectos/documentos/" \
    --content-length 0

# Temporal (para uploads en progreso)
awslocal s3api put-object \
    --bucket "$BUCKET_NAME" \
    --key "temp/" \
    --content-length 0

echo "✅ Estructura de carpetas creada"

# Listar buckets y contenido
echo ""
echo "📋 Buckets disponibles:"
awslocal s3 ls

echo ""
echo "📋 Contenido del bucket '$BUCKET_NAME':"
awslocal s3 ls "s3://$BUCKET_NAME" --recursive

echo ""
echo "=================================================="
echo "✅ LocalStack inicializado correctamente"
echo "=================================================="
echo ""
echo "🔗 Endpoints disponibles:"
echo "   - S3 API: http://localhost:4566"
echo "   - Bucket: s3://$BUCKET_NAME"
echo ""
echo "🧪 Prueba de conexión:"
echo "   aws --endpoint-url=http://localhost:4566 s3 ls"
echo ""