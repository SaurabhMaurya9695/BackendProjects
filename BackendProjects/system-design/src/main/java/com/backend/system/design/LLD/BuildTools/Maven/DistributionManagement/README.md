# `<distributionManagement>` in pom.xml

## The Problem First

You built a library (a JAR). Your team wants to use it in other projects.

How do they get it?

Option A — Copy the JAR manually into every project. ❌ (breaks with updates)
Option B — Push it to a **shared artifact repository** so everyone can declare it as a dependency. ✅

```
Your project                 Artifact Repository         Other Projects
────────────────             ────────────────────        ─────────────────
mvn deploy       ──────────► Nexus / Artifactory  ◄───── <dependency>
                             stores your JAR              in their pom.xml
```

**`<distributionManagement>` tells Maven: "where to push the artifact when I run `mvn deploy`."**

---

## What is `<distributionManagement>`?

It is a `pom.xml` tag that configures **where Maven publishes your built artifacts**.

Without it, `mvn deploy` fails with:
```
[ERROR] Failed to execute goal deploy: Repository element was not specified
```

With it, Maven knows exactly which server to upload to.

---

## Basic Structure

```xml
<distributionManagement>

    <!-- Where to publish RELEASE versions (1.0.0, 2.3.1) -->
    <repository>
        <id>releases</id>
        <name>Company Release Repository</name>
        <url>https://nexus.mycompany.com/repository/maven-releases/</url>
    </repository>

    <!-- Where to publish SNAPSHOT versions (1.0.0-SNAPSHOT) -->
    <snapshotRepository>
        <id>snapshots</id>
        <name>Company Snapshot Repository</name>
        <url>https://nexus.mycompany.com/repository/maven-snapshots/</url>
    </snapshotRepository>

</distributionManagement>
```

Two separate repos — one for stable releases, one for in-progress snapshots.

---

## Release vs Snapshot — What's the difference?

### SNAPSHOT version
```xml
<version>1.0.0-SNAPSHOT</version>
```

- Suffix `-SNAPSHOT` means **work in progress** — not stable yet
- Every `mvn deploy` **overwrites** the previous snapshot
- Maven checks for updates on every build (or daily, configurable)
- Use during active development

```
Day 1: deploy 1.0.0-SNAPSHOT → nexus stores it
Day 2: fix a bug, deploy again → nexus overwrites with latest
Day 3: consumer runs mvn build → gets latest snapshot automatically
```

### RELEASE version
```xml
<version>1.0.0</version>
```

- No `-SNAPSHOT` suffix = **stable, published release**
- **Immutable** — once deployed, cannot be overwritten (Nexus/Artifactory enforces this)
- Maven caches it locally — never re-downloads unless cache is cleared
- Use when the version is ready for other teams/production

```
Release 1.0.0 deployed → locked forever
If you need to fix a bug → release 1.0.1 (new version)
You CANNOT redeploy 1.0.0 ← Nexus will reject it
```

---

## The `<id>` Field — Why it matters

The `<id>` in `<repository>` must match a `<server>` entry in your
`~/.m2/settings.xml`. This is how Maven gets the credentials to authenticate
with the repository server.

```xml
<!-- pom.xml -->
<distributionManagement>
    <repository>
        <id>releases</id>   ← must match settings.xml server id
        <url>https://nexus.mycompany.com/repository/maven-releases/</url>
    </repository>
</distributionManagement>
```

```xml
<!-- ~/.m2/settings.xml -->
<settings>
    <servers>
        <server>
            <id>releases</id>   ← matches pom.xml id
            <username>deploy-user</username>
            <password>secret123</password>
        </server>
    </servers>
</settings>
```

Credentials stay in `settings.xml` (on the developer's machine or CI server),
never in `pom.xml` (which is committed to Git).

---

## What `mvn deploy` actually does

`mvn deploy` is the last phase in Maven's default lifecycle:

```
validate → compile → test → package → verify → install → deploy
                                          ↑              ↑
                                   runs unit tests   uploads to
                                                  remote repository
```

When you run `mvn deploy`:
1. Compiles the code
2. Runs tests
3. Packages into JAR/WAR
4. `install` — copies to your local `~/.m2/repository`
5. `deploy` — uploads to the remote repository defined in `<distributionManagement>`

---

## Real-World Example — Multi-Module Project

In a company, a parent POM defines `<distributionManagement>` once.
All child modules inherit it automatically.

```xml
<!-- parent pom.xml -->
<groupId>com.mycompany</groupId>
<artifactId>platform-parent</artifactId>
<version>2.5.0</version>
<packaging>pom</packaging>

<distributionManagement>
    <repository>
        <id>company-releases</id>
        <url>https://nexus.mycompany.com/repository/maven-releases/</url>
    </repository>
    <snapshotRepository>
        <id>company-snapshots</id>
        <url>https://nexus.mycompany.com/repository/maven-snapshots/</url>
    </snapshotRepository>
</distributionManagement>
```

```xml
<!-- child module pom.xml — inherits distributionManagement, no need to repeat -->
<parent>
    <groupId>com.mycompany</groupId>
    <artifactId>platform-parent</artifactId>
    <version>2.5.0</version>
</parent>

<artifactId>order-service</artifactId>
```

Running `mvn deploy` on the child uploads to the parent's configured repo.

---

## Publishing to Maven Central (Open Source)

If your library is open source and you want the world to use it via Maven Central:

```xml
<distributionManagement>
    <repository>
        <id>ossrh</id>
        <url>https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/</url>
    </repository>
    <snapshotRepository>
        <id>ossrh</id>
        <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
    </snapshotRepository>
</distributionManagement>
```

Requirements for Maven Central:
- Source JAR (`sources.jar`)
- Javadoc JAR (`javadoc.jar`)
- GPG-signed artifacts
- Valid `<licenses>`, `<developers>`, `<scm>` tags in pom.xml

---

## Site Distribution (Optional)

`<distributionManagement>` can also define where to publish your project's
**documentation site** (generated by `mvn site`):

```xml
<distributionManagement>
    <repository>...</repository>

    <site>
        <id>project-site</id>
        <url>scp://docs.mycompany.com/var/www/project-docs</url>
    </site>
</distributionManagement>
```

Running `mvn site-deploy` uploads the generated HTML docs to this location.
Rarely used in modern teams (most use GitHub Pages or Confluence instead).

---

## Common Errors and Fixes

### Error 1 — No repository configured
```
[ERROR] Failed to execute goal deploy:
        Repository element was not specified in the POM
```
**Fix:** Add `<distributionManagement>` to your `pom.xml`.

---

### Error 2 — 401 Unauthorized
```
[ERROR] Failed to deploy: Return code is: 401, ReasonPhrase: Unauthorized
```
**Fix:** Check `~/.m2/settings.xml` — the `<server>` `<id>` must match the
`<repository>` `<id>` in `pom.xml`, and credentials must be correct.

---

### Error 3 — 400 Bad Request (trying to redeploy a release)
```
[ERROR] Failed to deploy: Return code is: 400
        Cannot deploy artifact, as the repository does not allow updating assets
```
**Fix:** You're trying to redeploy a release version. Release repos are immutable.
Bump the version number (`1.0.0` → `1.0.1`) and deploy again.

---

### Error 4 — Deploying snapshot to release repo
```
[ERROR] Deployment failed: repository does not allow snapshot artifacts
```
**Fix:** Your version ends in `-SNAPSHOT` but you're pointing at the releases repo.
Either remove `-SNAPSHOT` from version (for a real release) or deploy to
`<snapshotRepository>` instead.

---

## distributionManagement vs repositories

These two are often confused:

| Tag | Purpose | Direction |
|-----|---------|-----------|
| `<distributionManagement>` | Where to **publish** your artifacts | Outgoing (upload) |
| `<repositories>` | Where to **download** dependencies from | Incoming (download) |

```xml
<!-- Where you DOWNLOAD dependencies from -->
<repositories>
    <repository>
        <id>central</id>
        <url>https://repo.maven.apache.org/maven2</url>
    </repository>
</repositories>

<!-- Where you UPLOAD your artifacts to -->
<distributionManagement>
    <repository>
        <id>releases</id>
        <url>https://nexus.mycompany.com/repository/maven-releases/</url>
    </repository>
</distributionManagement>
```

---

## Summary

```
What:   <distributionManagement> tells Maven WHERE to upload
        your artifact when you run mvn deploy.

Why:    So your team (or the world) can use your library
        as a <dependency> in their pom.xml.

Two repos:
  <repository>         → stable releases (immutable, versioned)
  <snapshotRepository> → work-in-progress (overwritable, -SNAPSHOT)

Key rule:
  <id> in pom.xml  must match  <id> in ~/.m2/settings.xml
  (that's how Maven finds the credentials)

Commands:
  mvn deploy       → compile + test + package + upload to remote repo
  mvn install      → compile + test + package + save to LOCAL ~/.m2 only
                     (does NOT upload anywhere)
```

---

## Quick Reference

```xml
<distributionManagement>

    <!-- Stable releases — immutable once deployed -->
    <repository>
        <id>releases</id>
        <url>https://your-nexus/repository/maven-releases/</url>
    </repository>

    <!-- In-progress snapshots — overwritten on every deploy -->
    <snapshotRepository>
        <id>snapshots</id>
        <url>https://your-nexus/repository/maven-snapshots/</url>
    </snapshotRepository>

</distributionManagement>
```

```xml
<!-- ~/.m2/settings.xml — credentials (never in pom.xml) -->
<servers>
    <server>
        <id>releases</id>
        <username>your-username</username>
        <password>your-password</password>
    </server>
    <server>
        <id>snapshots</id>
        <username>your-username</username>
        <password>your-password</password>
    </server>
</servers>
```
