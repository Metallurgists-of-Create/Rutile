<div align="center">
  <img src="https://metallurgists-of-create.github.io/assets/rutile-icon-small.webp">
  <h1>Rutile</h1>
  <a href="https://discord.gg/4yx2D6XRKf"><picture><source srcset="https://img.shields.io/badge/Discord-202830?style=for-the-badge&logo=discord" media="(prefers-color-scheme: dark)"><img src="https://img.shields.io/badge/Discord-white?style=for-the-badge&logo=discord" alt="Discord"></picture></a>
  <br>
  <a href="https://github.com/Metallurgists-of-Create/Rutile/graphs/contributors"><picture><img alt="GitHub contributors" src="https://img.shields.io/github/contributors/Metallurgists-of-Create/Rutile"></picture></a>
  <a href="https://github.com/Metallurgists-of-Create/Rutile/stargazers"><picture><img alt="Stars" src="https://img.shields.io/github/stars/Metallurgists-of-Create/Rutile?style=flat"></picture></a>
  <a href="https://github.com/Metallurgists-of-Create/Rutile/releases/latest"><picture><img alt="Latest Release" src="https://img.shields.io/github/v/release/Metallurgists-of-Create/Rutile"></picture></a>
  <a href="https://github.com/Metallurgists-of-Create/Rutile/commits/"><picture><img alt="Commit activity" src="https://img.shields.io/github/commit-activity/t/Metallurgists-of-Create/Rutile"></picture></a>
  <br>
  <a href="https://github.com/Metallurgists-of-Create/Rutile/issues"><picture><img alt="Open Issues" src="https://img.shields.io/github/issues-raw/Metallurgists-of-Create/Rutile"></picture></a>
  <a href="https://github.com/Metallurgists-of-Create/Rutile/issues?q=is%3Aissue+state%3Aclosed"><picture><img alt="Closed Issues" src="https://img.shields.io/github/issues-closed-raw/Metallurgists-of-Create/Rutile"></picture></a>
  <a href="https://github.com/Metallurgists-of-Create/Rutile/pulls"><picture><img alt="Pull Requests" src="https://img.shields.io/github/issues-pr-raw/Metallurgists-of-Create/Rutile"></picture></a>
  <a href="https://github.com/Metallurgists-of-Create/Rutile/pulls?q=is%3Apr+state%3Aclosed"><picture><img alt="Closed Pull Requests" src="https://img.shields.io/github/issues-pr-closed-raw/Metallurgists-Of-Create/Rutile"></picture></a>
  <br>
  <a>Element Compositions and other useful tools</a>
</div>


## How to implement
\
Create a plugin:
```java
@RutilePlugin
public class YourRutilePlugin implements IRutilePlugin {

    @Override
    public void configure(PluginConfig config) {
        config.setModId("namespace");
        config.setRegistrate(YourMod.registrate());
    }
}
```
\
Register Elements:
```java
public class YourElements {
    public static final ElementLike ELEMENTIUM = RutileElements.create("yourmod:elementium", "El", 0xff4aedd9);

    public static void init() {}
}
```
