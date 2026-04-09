-- ===================================
-- Script SQL para NeonTech PostgreSQL
-- Biblioteca DUOC - Testing
-- ===================================

-- 1. Verificar que la tabla fue creada por Hibernate
SELECT table_name, column_name, data_type, character_maximum_length
FROM information_schema.columns
WHERE table_name = 'libros'
ORDER BY ordinal_position;

-- 2. Ver todos los libros (debería estar vacío inicialmente)
SELECT * FROM libros;

-- 3. Insertar libros de prueba manualmente (opcional)
INSERT INTO libros (isbn, titulo, editorial, fecha_publicacion, autor)
VALUES
    ('978-0-13-468599-1', 'Clean Code', 'Prentice Hall', 2008, 'Robert C. Martin'),
    ('978-0-596-52068-7', 'Head First Design Patterns', 'O''Reilly', 2004, 'Eric Freeman'),
    ('978-0-13-235088-4', 'The Pragmatic Programmer', 'Addison-Wesley', 2019, 'David Thomas');

-- 4. Verificar inserción
SELECT id, titulo, autor FROM libros;

-- 5. Buscar por autor
SELECT * FROM libros WHERE autor LIKE '%Martin%';

-- 6. Contar libros
SELECT COUNT(*) as total_libros FROM libros;

-- 7. Actualizar un libro
UPDATE libros
SET editorial = 'Pearson'
WHERE isbn = '978-0-13-468599-1';

-- 8. Eliminar un libro (cuidado en producción!)
-- DELETE FROM libros WHERE id = 1;

-- 9. Ver estructura completa de la tabla
\d libros

-- 10. Limpiar tabla (⚠️ PELIGRO - borra todo)
-- TRUNCATE TABLE libros;

-- 11. Eliminar tabla (⚠️ PELIGRO - Hibernate la recreará)
-- DROP TABLE libros;
