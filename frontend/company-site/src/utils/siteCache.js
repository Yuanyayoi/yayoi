import { ref } from "vue";
import { siteApi } from "@/api";
import axios from "axios";

const cache = {
  global: ref(null),
  footer: ref(null),
  contact: ref(null),
  about: ref(null),
  carousels: ref(null),
  features: ref(null),
  icons: ref(null),
  homeNewsList: ref(null),
  homeProductList: ref(null),
  modules: ref(null),
  featuresText: ref(null),
  homeAboutIntro: ref(null),
  homeNews: ref(null),
  homeProducts: ref(null),
  homeCta: ref(null),
  productHero: ref(null),
  newsHero: ref(null),
  contactHero: ref(null),
  contactOnline: ref(null),
  contactForm: ref(null),
  aboutHero: ref(null),
  values: ref(null),
  milestones: ref(null),
  coreValues: ref(null),
  awards: ref(null),
  parentCategories: ref(null),
  newsCategories: ref(null),
};

const loading = {};
const CACHE_TTL = 5 * 60 * 1000;
const timestamps = {};

const isExpired = (key) => {
  const ts = timestamps[key];
  if (!ts) return true;
  return Date.now() - ts > CACHE_TTL;
};

const getOrLoad = async (key, loader) => {
  if (cache[key].value !== null && !isExpired(key)) {
    return cache[key].value;
  }
  if (loading[key]) {
    return loading[key];
  }
  loading[key] = (async () => {
    try {
      const data = await loader();
      cache[key].value = data;
      timestamps[key] = Date.now();
      return data;
    } finally {
      delete loading[key];
    }
  })();
  return loading[key];
};

const stripHostPrefix = (url) => {
  if (typeof url === "string" && url.startsWith("http")) {
    const parsed = new URL(url);
    return url.replace(parsed.origin, "");
  }
  return url;
};

const normalizeImageUrls = (data) => {
  if (!data || typeof data !== "object") return data;

  Object.keys(data).forEach((key) => {
    if (
      key.toLowerCase().includes("image") ||
      key.toLowerCase().includes("url")
    ) {
      data[key] = stripHostPrefix(data[key]);
    }
    if (typeof data[key] === "object") {
      normalizeImageUrls(data[key]);
    }
  });

  if (Array.isArray(data)) {
    data.forEach((item) => normalizeImageUrls(item));
  }

  return data;
};

const fetchSiteData = async (path) => {
  const res = await axios.get(path);
  return normalizeImageUrls(res.data?.data || {});
};

export const siteCache = {
  async getGlobal() {
    return getOrLoad("global", () => fetchSiteData("/site/global"));
  },
  async getFooter() {
    return getOrLoad("footer", () => fetchSiteData("/site/footer"));
  },
  async getContact() {
    return getOrLoad("contact", () => fetchSiteData("/site/contact"));
  },
  async getAbout() {
    return getOrLoad("about", () => fetchSiteData("/site/about"));
  },
  async getCarousels() {
    return getOrLoad("carousels", async () => {
      const res = await axios.get("/api/carousels");
      if (res.data.success && res.data.data) {
        return res.data.data.map((c) => ({
          title: c.title,
          subtitle: c.subtitle || "",
          description: c.description || "",
          image: stripHostPrefix(c.imageUrl),
        }));
      }
      return [];
    });
  },
  async getFeatures() {
    return getOrLoad("features", async () => {
      const res = await axios.get("/api/features");
      return res.data.success && res.data.data ? res.data.data : [];
    });
  },
  async getIcons() {
    return getOrLoad("icons", async () => {
      const res = await axios.get("/api/icons/public");
      const map = {};
      if (res.data.success && res.data.data) {
        res.data.data.forEach((icon) => {
          map[icon.id] = icon;
        });
      }
      return map;
    });
  },
  async getHomeNewsList() {
    return getOrLoad("homeNewsList", async () => {
      const res = await axios.get("/articles/public/home");
      return res.data && res.data.code === 200 ? res.data.data || [] : [];
    });
  },
  async getHomeProductList() {
    return getOrLoad("homeProductList", async () => {
      const res = await axios.get("/api/products/public/home");
      return res.data && res.data.code === 200 ? res.data.data || [] : [];
    });
  },
  async getModules() {
    return getOrLoad("modules", () => fetchSiteData("/site/modules"));
  },
  async getFeaturesText() {
    return getOrLoad("featuresText", () => fetchSiteData("/site/features"));
  },
  async getHomeAboutIntro() {
    return getOrLoad("homeAboutIntro", () =>
      fetchSiteData("/site/home-about-intro"),
    );
  },
  async getHomeNews() {
    return getOrLoad("homeNews", () => fetchSiteData("/site/home-news"));
  },
  async getHomeProducts() {
    return getOrLoad("homeProducts", () =>
      fetchSiteData("/site/home-products"),
    );
  },
  async getHomeCta() {
    return getOrLoad("homeCta", () => fetchSiteData("/site/home-cta"));
  },
  async getProductHero() {
    return getOrLoad("productHero", () => fetchSiteData("/site/product-hero"));
  },
  async getNewsHero() {
    return getOrLoad("newsHero", () => fetchSiteData("/site/news-hero"));
  },
  async getContactHero() {
    return getOrLoad("contactHero", () => fetchSiteData("/site/contact-hero"));
  },
  async getContactOnline() {
    return getOrLoad("contactOnline", () =>
      fetchSiteData("/site/contact-online"),
    );
  },
  async getContactForm() {
    return getOrLoad("contactForm", () => fetchSiteData("/site/contact-form"));
  },
  async getAboutHero() {
    return getOrLoad("aboutHero", async () => {
      try {
        const res = await axios.get("/api/site/about-hero");
        return res.data?.code === 200 ? res.data.data : {};
      } catch {
        return {};
      }
    });
  },
  async getValues() {
    return getOrLoad("values", () => fetchSiteData("/site/values"));
  },
  async getMilestones() {
    return getOrLoad("milestones", async () => {
      const res = await axios.get("/api/milestones/public");
      return res.data?.code === 200 ? res.data.data || [] : [];
    });
  },
  async getCoreValues() {
    return getOrLoad("coreValues", async () => {
      const res = await axios.get("/api/core-values/public");
      return res.data?.code === 200 ? res.data.data || [] : [];
    });
  },
  async getAwards() {
    return getOrLoad("awards", async () => {
      const res = await axios.get("/api/awards/public");
      return res.data?.code === 200 ? res.data.data || [] : [];
    });
  },
  async getParentCategories() {
    return getOrLoad("parentCategories", async () => {
      const res = await axios.get("/categories/public");
      return res.data?.code === 200 ? res.data.data || [] : [];
    });
  },
  async getNewsCategories() {
    return getOrLoad("newsCategories", async () => {
      const res = await axios.get("/api/news-categories/public");
      return res.data?.code === 200 ? res.data.data || [] : [];
    });
  },
  invalidate(key) {
    if (key) {
      if (cache[key]) {
        cache[key].value = null;
        delete timestamps[key];
      }
    } else {
      Object.keys(cache).forEach((k) => {
        cache[k].value = null;
        delete timestamps[k];
      });
    }
  },
  refs: cache,
};
