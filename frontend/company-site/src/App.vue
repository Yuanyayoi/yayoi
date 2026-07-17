<template>
  <div class="app">
    <Header />
    <main>
      <router-view />
    </main>
    <Footer />
    <BackToTop />
  </div>
</template>

<script setup>
import { onMounted, onUnmounted } from "vue";
import { useRouter } from "vue-router";
import Header from "./components/Header.vue";
import Footer from "./components/Footer.vue";
import BackToTop from "./components/BackToTop.vue";
import { siteCache } from "./utils/siteCache";

const router = useRouter();

const faviconSet = { value: false };

const updateFavicon = (logoUrl) => {
  if (!logoUrl || faviconSet.value) return;
  const head = document.getElementsByTagName("head")[0];
  const oldIcons = document.querySelectorAll(
    "link[rel='icon'], link[rel='shortcut icon']"
  );
  oldIcons.forEach((link) => link.parentNode?.removeChild(link));

  const fullUrl = logoUrl.startsWith("http")
    ? logoUrl
    : window.location.origin + logoUrl;

  const newLink = document.createElement("link");
  newLink.rel = "icon";
  newLink.type = "image/png";
  newLink.href = fullUrl;
  head.appendChild(newLink);

  const newLink2 = document.createElement("link");
  newLink2.rel = "shortcut icon";
  newLink2.type = "image/png";
  newLink2.href = fullUrl;
  head.appendChild(newLink2);

  faviconSet.value = true;
};

const loadGlobalSettings = async () => {
  try {
    const settings = await siteCache.getGlobal();
    if (settings.siteName) {
      document.title = settings.siteName;
    }
    if (settings.logoUrl) {
      updateFavicon(settings.logoUrl);
    }
  } catch (e) {
    console.error("加载全局设置失败", e);
  }
};

const handleRouteChange = () => {
  window.scrollTo(0, 0);
  document.body.style.overflow = '';
  document.body.style.touchAction = '';
};

onMounted(() => {
  loadGlobalSettings();
  router.afterEach(handleRouteChange);
});

onUnmounted(() => {
  router.afterEach(() => {});
});
</script>

<style scoped>
.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

main {
  flex: 1;
}
</style>