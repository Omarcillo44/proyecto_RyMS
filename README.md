# 🎯 Sistema de Líneas de Espera M/M/1 y M/M/s

> **Análisis y Simulación de Modelos de Colas mediante Teoría Matemática y Eventos Discretos**

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21-blue.svg)](https://openjfx.io/)
[![License](https://img.shields.io/badge/License-Academic-green.svg)]()

## 📋 Tabla de Contenidos

- [Descripción](#-descripción)
- [Características](#-características)
- [Requisitos del Sistema](#-requisitos-del-sistema)
- [Instalación y Ejecución](#-instalación-y-ejecución)
- [Uso Rápido](#-uso-rápido)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Casos de Uso](#-casos-de-uso)
- [Solución de Problemas](#-solución-de-problemas)
- [Créditos](#-créditos)

---

## 📖 Descripción

Sistema desarrollado en Java con interfaz JavaFX para el análisis teórico y simulación de sistemas de líneas de espera (teoría de colas). Implementa dos modelos fundamentales:

- **M/M/1**: Sistema con un único servidor
- **M/M/s**: Sistema con múltiples servidores en paralelo

El sistema combina:
1. **Solución Analítica**: Cálculo de métricas teóricas mediante fórmulas matemáticas
2. **Simulación de Eventos Discretos**: Modelado del comportamiento real del sistema
3. **Comparación y Validación**: Verificación de resultados mediante Ley de Little e intervalos de confianza

### 🎓 Contexto Académico

- **Materia**: Redes y Modelos de Simulación
- **Institución**: UPIICSA - Instituto Politécnico Nacional
- **Objetivo**: Proyecto final de curso

---

## ✨ Características

### Análisis Teórico
- ✅ Cálculo de métricas de desempeño (Lq, L, Wq, W, ρ, Erlang C)
- ✅ Validación de estabilidad del sistema
- ✅ Probabilidades opcionales: P(n>k), P(Wq>t), P(Ws>t)
- ✅ Soporte para parámetros personalizados

### Simulación
- ✅ Simulación de eventos discretos con generación de números aleatorios
- ✅ Reproducibilidad mediante semillas controladas
- ✅ Periodo de warm-up automático para M/M/s (eliminación de fase transitoria)
- ✅ Comparación automática con resultados teóricos
- ✅ Cálculo de intervalos de confianza al 95%
- ✅ Métricas avanzadas (valores máximos, distribuciones, balance de carga)

### Visualización
- ✅ Interfaz gráfica intuitiva con JavaFX
- ✅ Tablas detalladas cliente por cliente
- ✅ Comparación visual analítico vs simulado
- ✅ Exportación de datos a CSV
- ✅ Modo consola integrado para debugging

---

## 💻 Requisitos del Sistema

### Requisitos Obligatorios

| Componente | Versión Mínima | Recomendada | Verificación |
|------------|----------------|-------------|--------------|
| **Java JDK** | 17 | 21 | `java --version` |
| **JavaFX SDK** | 17 | 21 | Incluido en JAR |
| **Sistema Operativo** | Windows 10, macOS 10.15, Linux | Cualquier SO moderno | - |
| **RAM** | 512 MB | 1 GB | - |
| **Espacio en Disco** | 100 MB | 200 MB | - |

### Verificar Instalación de Java

Abre una terminal/CMD y ejecuta:

```bash
java --version
```

**Salida esperada**:
```
java 21.0.1 2023-10-17 LTS
Java(TM) SE Runtime Environment (build 21.0.1+12-LTS-29)
Java HotSpot(TM) 64-Bit Server VM (build 21.0.1+12-LTS-29, mixed mode, sharing)
```

⚠️ **Si no tienes Java instalado**:
- Descarga desde: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) o [OpenJDK](https://adoptium.net/)
- Instala siguiendo las instrucciones del instalador
- Reinicia tu terminal después de instalar

---

## 🚀 Instalación y Ejecución

### Opción 1: Ejecutar JAR Precompilado (Recomendado)

#### Windows

1. **Descarga el archivo** `proyecto-ryms.jar`

2. **Abre CMD o PowerShell** en la carpeta donde descargaste el JAR:
   - Opción A: Click derecho en la carpeta → "Abrir en Terminal"
   - Opción B: Presiona `Win + R`, escribe `cmd`, navega con `cd` a la carpeta

3. **Ejecuta el comando**:
   ```cmd
   java -jar proyecto-ryms.jar
   ```

4. **Si aparece error "JavaFX no encontrado"**, usa:
   ```cmd
   java --module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml -jar proyecto-ryms.jar
   ```
   (Reemplaza `C:\path\to\javafx-sdk\lib` con la ruta real de tu JavaFX SDK)

#### macOS / Linux

1. **Abre Terminal** en la carpeta del JAR:
   ```bash
   cd /ruta/donde/descargaste
   ```

2. **Dale permisos de ejecución** (solo primera vez):
   ```bash
   chmod +x proyecto-ryms.jar
   ```

3. **Ejecuta**:
   ```bash
   java -jar proyecto-ryms.jar
   ```

4. **Si aparece error de JavaFX**:
   ```bash
   java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -jar proyecto-ryms.jar
   ```

### Opción 2: Compilar desde Código Fuente

#### Prerrequisitos
- Maven 3.8+ instalado
- JDK 21 configurado como `JAVA_HOME`

#### Pasos

1. **Clona el repositorio** (si aplica):
   ```bash
   git clone [URL_DEL_REPOSITORIO]
   cd proyecto-ryms
   ```

2. **Compila el proyecto**:
   ```bash
   mvn clean package
   ```

3. **Ejecuta**:
   ```bash
   java -jar target/proyecto-ryms-1.0.jar
   ```

### Opción 3: Ejecutar desde IDE

1. **Importa el proyecto** en IntelliJ IDEA, Eclipse o NetBeans
2. **Configura JavaFX** en las opciones del proyecto
3. **Ejecuta la clase principal**: `com.omarcisho.proyecto_ryms.HelloApplication`

---

## 📱 Uso Rápido

### Inicio de la Aplicación

Al ejecutar el JAR, verás dos opciones:

1. **Interfaz Gráfica (GUI)**: Se abre automáticamente
2. **Modo Consola**: Continúa ejecutándose en segundo plano para debugging

### Flujo Básico - Sistema M/M/1

```
1. [Menú Principal] → Seleccionar "Sistema M/M/1"
2. [Analítico] → Ingresar λ=9, μ=12 → [Calcular]
3. Revisar resultados teóricos
4. [Ir a Simulación] → Ingresar N=1000 → [Ejecutar]
5. Comparar resultados analítico vs simulado
6. [Ver Tabla Detallada] → Revisar datos cliente por cliente
```

### Flujo Básico - Sistema M/M/s

```
1. [Menú Principal] → Seleccionar "Sistema M/M/s"
2. [Analítico] → Ingresar λ=25, μ=7, s=4 → [Calcular]
3. Revisar resultados teóricos (incluye Erlang C)
4. [Ir a Simulación] → Ingresar N=2000 → [Ejecutar]
   ⚠️ Sistema aplica warm-up automático (20% de N)
5. Revisar utilización por servidor y balance de carga
6. [Ver Tabla Detallada] → Columnas dinámicas según s
```

### Ejemplo Rápido: Lava Coches

**Problema**: Un lavado de autos puede atender 12 autos/hora. Llegan 9 autos/hora. ¿Cuánto esperarán?

**Solución**:
1. Abrir Sistema M/M/1
2. Ingresar: λ=9, μ=12
3. Resultado: **Wq = 0.25 horas = 15 minutos** ✅

---

## 📁 Estructura del Proyecto

```
proyecto-ryms/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── analitico/              # Cálculos teóricos
│   │   │   │   ├── MM1Calculadora.java
│   │   │   │   ├── MMsCalculadora.java
│   │   │   │   ├── ResultadoAnaliticoMM1.java
│   │   │   │   └── ResultadoAnaliticoMMs.java
│   │   │   ├── simulacion/             # Simulación de eventos
│   │   │   │   ├── SimuladorMM1.java
│   │   │   │   ├── SimuladorMMs.java
│   │   │   │   ├── ResultadoSimulacionMM1.java
│   │   │   │   └── ResultadoSimulacionMMs.java
│   │   │   ├── modelo/                 # Entidades del sistema
│   │   │   │   ├── Cliente.java
│   │   │   │   ├── Evento.java
│   │   │   │   ├── Servidor.java
│   │   │   │   └── ColaEventos.java
│   │   │   ├── util/                   # Utilidades
│   │   │   │   ├── GeneradorExponencial.java
│   │   │   │   ├── Estadisticas.java
│   │   │   │   └── ValidadorParametros.java
│   │   │   ├── controller/             # Controladores JavaFX
│   │   │   │   ├── MenuPrincipalController.java
│   │   │   │   ├── MM1AnaliticoController.java
│   │   │   │   ├── MM1SimulacionController.java
│   │   │   │   └── [otros controladores...]
│   │   │   └── com.omarcisho.proyecto_ryms/
│   │   │       └── HelloApplication.java  # Clase principal
│   │   └── resources/
│   │       └── com/omarcisho/proyecto_ryms/
│   │           ├── MenuPrincipal.fxml
│   │           ├── MM1Analitico.fxml
│   │           ├── MM1Simulacion.fxml
│   │           └── [otros FXML...]
│   └── test/                           # Tests unitarios
├── target/
│   └── proyecto-ryms-1.0.jar          # JAR ejecutable
├── pom.xml                             # Configuración Maven
├── README.md                           # Este archivo
└── Documentacion_Tecnica.md           # Documentación completa
```

---

## 💡 Casos de Uso

### Caso 1: Optimización de Recursos

**Problema**: ¿Cuántos cajeros necesito en mi banco?

**Solución**:
1. Medir λ (clientes/hora) durante una semana
2. Estimar μ (capacidad promedio de un cajero)
3. Probar diferentes valores de s:
   - s=3: ρ=95% → Sistema saturado ❌
   - s=4: ρ=89% → Viable pero justo ⚠️
   - s=5: ρ=71% → Óptimo ✅

### Caso 2: Validación de SLA

**Problema**: Prometimos "atención en menos de 10 minutos"

**Solución**:
1. Calcular analítico con λ y μ actuales
2. Usar parámetro opcional: P(Wq>10min)
3. Si P(Wq>10min) > 5% → No cumplimos SLA
4. Simular con más servidores hasta cumplir

### Caso 3: Análisis de Capacidad

**Problema**: ¿Se llenará mi sala de espera de 20 personas?

**Solución**:
1. Usar parámetro opcional: P(n>20)
2. Si P(n>20) > 10% → Ampliar sala
3. Simular para validar

---

## 🔧 Solución de Problemas

### Error: "No se encuentra el comando java"

**Causa**: Java no instalado o no está en PATH

**Solución**:
1. Instalar JDK 21 desde [Oracle](https://www.oracle.com/java/technologies/downloads/)
2. Verificar instalación: `java --version`
3. Si sigue sin funcionar:
   - **Windows**: Agregar `C:\Program Files\Java\jdk-21\bin` al PATH
   - **macOS/Linux**: Agregar `export JAVA_HOME=/path/to/jdk` en `.bashrc` o `.zshrc`

### Error: "JavaFX runtime components are missing"

**Causa**: JavaFX no incluido en el runtime

**Solución Rápida**:
```bash
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -jar proyecto-ryms.jar
```

**Solución Permanente (Windows)**:
1. Descargar JavaFX SDK desde [openjfx.io](https://openjfx.io/)
2. Extraer en `C:\javafx-sdk-21`
3. Crear un archivo `run.bat` con:
   ```batch
   @echo off
   java --module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml -jar proyecto-ryms.jar
   pause
   ```
4. Ejecutar haciendo doble clic en `run.bat`

**Solución Permanente (macOS/Linux)**:
1. Descargar y extraer JavaFX SDK
2. Crear script `run.sh`:
   ```bash
   #!/bin/bash
   java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -jar proyecto-ryms.jar
   ```
3. Dar permisos: `chmod +x run.sh`
4. Ejecutar: `./run.sh`

### Error: "Sistema inestable (λ ≥ μ)"

**Causa**: Parámetros inválidos

**Solución**: Verificar que:
- M/M/1: λ < μ
- M/M/s: λ < s·μ

### Error de Simulación Alto (>10%)

**Causa**: N muy pequeño para el nivel de utilización

**Solución**:
- ρ < 0.7: N=1000
- 0.7 ≤ ρ < 0.9: N=2000-5000
- ρ ≥ 0.9: N=10000+

### La ventana no se abre

**Solución**:
1. Verificar que no haya errores en consola
2. Intentar con otro runtime de Java
3. Revisar que el puerto gráfico esté disponible

---

## 📚 Documentación Adicional

Para información detallada sobre:
- Arquitectura del sistema
- Modelos matemáticos
- Guías de uso completas
- Interpretación de resultados

Consultar: **`Documentacion_Tecnica.md`**

---

## 🧪 Tests y Validación

### Casos de Prueba Incluidos

El sistema incluye validación automática con los siguientes casos de referencia:

| Caso | λ | μ | s | ρ | Resultado Esperado |
|------|---|---|---|---|-------------------|
| Lava coches | 9 | 12 | 1 | 0.75 | Wq=0.25, L=3.0 |
| Banco | 25 | 7 | 4 | 0.893 | Wq=0.257, Lq=6.43 |
| Sistema inestable | 15 | 10 | 1 | 1.5 | Error: "Sistema inestable" |

### Ejecutar Tests

```bash
mvn test
```

---

## 🤝 Contribuciones

Este es un proyecto académico. Para sugerencias o reportar bugs:

1. **Abrir un Issue** en el repositorio (si aplica)
2. **Contactar al equipo** mediante correo institucional
3. **Fork y Pull Request** para mejoras

---

## 📄 Licencia

Este proyecto es de uso **académico** para la materia de Redes y Modelos de Simulación.

**Prohibido**:
- ❌ Uso comercial
- ❌ Redistribución sin atribución

**Permitido**:
- ✅ Uso educativo y de aprendizaje
- ✅ Modificaciones para proyectos académicos
- ✅ Referencia en trabajos citando la fuente

---

## 👥 Créditos

### Desarrollo
- **Equipo de Desarrollo**: [Nombres del equipo]
- **Institución**: UPIICSA - Instituto Politécnico Nacional
- **Materia**: Redes y Modelos de Simulación
- **Periodo**: Semestre 2024-2

### Tecnologías Utilizadas
- **Lenguaje**: Java 21
- **Framework UI**: JavaFX 21
- **Build Tool**: Maven 3.9
- **Control de Versiones**: Git

### Referencias Bibliográficas
- Hillier, F. S., & Lieberman, G. J. (2015). *Introduction to Operations Research*
- Law, A. M. (2015). *Simulation Modeling and Analysis*
- Ross, S. M. (2014). *Introduction to Probability Models*

---

## 📞 Contacto y Soporte

### Para Dudas Académicas
- **Profesor**: [Nombre del profesor]
- **Email**: [correo institucional]

### Para Soporte Técnico
- **Email del equipo**: [correo del equipo]
- **Horario de atención**: Lunes a Viernes, 9:00 - 17:00

---

## 🔄 Historial de Versiones

### v1.0.0 (Diciembre 2024)
- ✅ Implementación completa de M/M/1 y M/M/s
- ✅ Interfaz gráfica JavaFX
- ✅ Simulación de eventos discretos
- ✅ Comparación analítico vs simulado
- ✅ Tablas detalladas con columnas dinámicas
- ✅ Exportación a CSV
- ✅ Validación con Ley de Little
- ✅ Intervalos de confianza 95%
- ✅ Warm-up automático para M/M/s

### Futuras Mejoras Planeadas
- [ ] Gráficos de evolución temporal
- [ ] Soporte para G/G/1 (distribuciones generales)
- [ ] Análisis de sensibilidad automático
- [ ] Módulo de optimización (encontrar s óptimo)
- [ ] Exportación a PDF con reporte completo

---

<div align="center">

**Sistema de Líneas de Espera M/M/1 y M/M/s**

Desarrollado con ❤️ para UPIICSA - IPN

[Documentación](./Documentacion_Tecnica.md) • [Reportar Bug](#contacto-y-soporte) • [Solicitar Feature](#contacto-y-soporte)

</div>
