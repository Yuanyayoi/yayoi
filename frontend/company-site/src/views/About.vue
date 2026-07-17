<template>
  <div class="about-page">
    <!-- Hero Section -->
    <section class="hero" :style="heroBackgroundStyle">
      <div class="hero-overlay"></div>
      <div class="hero-bg-grid"></div>
      <div class="container">
        <div
          class="hero-content"
          v-if="!isLoading && (heroData.companyName || aboutData.companyName)"
        >
          <h1>{{ heroData.companyName || aboutData.companyName }}</h1>
          <p class="hero-description">
            {{ heroData.companyDesc || aboutData.companyDesc }}
          </p>
        </div>
        <div class="hero-loading" v-else>
          <div class="loading-spinner"></div>
        </div>
      </div>
    </section>

    <!-- Company Introduction -->
    <section class="about-detail">
      <div class="container">
        <div class="about-grid">
          <div class="about-text">
            <span class="section-subtitle">关于我们</span>
            <h2 v-if="aboutData.companyName">{{ aboutData.companyName }}</h2>
            <p v-if="aboutData.companyDesc">{{ aboutData.companyDesc }}</p>
            <div class="about-mission" v-if="aboutData.mission">
              <svg
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                width="20"
                height="20"
              >
                <circle cx="12" cy="12" r="10" />
                <path d="M12 6v6l4 2" />
              </svg>
              <span>{{ aboutData.mission }}</span>
            </div>
            <div class="about-vision" v-if="aboutData.vision">
              <svg
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                width="20"
                height="20"
              >
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
                <circle cx="12" cy="12" r="3" />
              </svg>
              <span>{{ aboutData.vision }}</span>
            </div>
          </div>
          <div class="about-image">
            <img
              :src="aboutData.image || smartEnergyImage"
              alt="公司形象展示"
            />
            <div class="image-accent"></div>
          </div>
        </div>
      </div>
    </section>

    <!-- Core Values -->
    <section class="core-values" v-if="coreValues.length">
      <div class="container">
        <div class="section-header">
          <span class="section-subtitle">企业文化</span>
          <h2>核心价值观</h2>
        </div>
        <div class="values-grid">
          <div class="value-card" v-for="value in coreValues" :key="value.id">
            <div class="value-icon">{{ value.icon || "🎯" }}</div>
            <h3>{{ value.title }}</h3>
            <p>{{ value.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Team Section -->
    <section class="team-section" v-if="teamMembers.length">
      <div class="container">
        <div class="section-header">
          <span class="section-subtitle">专业团队</span>
          <h2>核心团队</h2>
        </div>
        <div class="team-grid">
          <div class="team-card" v-for="member in teamMembers" :key="member.id">
            <div class="team-avatar">
              <img
                v-if="member.avatar"
                :src="member.avatar"
                :alt="member.name"
              />
              <div v-else class="avatar-placeholder">
                {{ member.name?.charAt(0) }}
              </div>
            </div>
            <h3>{{ member.name }}</h3>
            <p class="team-position">{{ member.position }}</p>
            <p class="team-bio">{{ member.bio }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Development History -->
    <section class="development-history" v-if="milestones.length">
      <div class="container">
        <div class="section-header">
          <span class="section-subtitle">公司发展历程及发展规划</span>
          <h2>公司成长轨迹与未来规划</h2>
        </div>
        <div class="timeline">
          <div class="timeline-line"></div>
          <div
            class="timeline-item"
            v-for="(milestone, index) in reversedMilestones"
            :key="milestone.id"
            :class="index % 2 === 1 ? 'right' : 'left'"
          >
            <div class="timeline-dot">
              <span>{{ milestone.year }}</span>
            </div>
            <div class="timeline-content">
              <h3 class="timeline-title">{{ milestone.title }}</h3>
              <p>{{ milestone.description }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Awards Section -->
    <section class="awards-section" v-if="awards.length">
      <div class="container">
        <div class="section-header">
          <span class="section-subtitle">荣誉资质</span>
          <h2>所获荣誉</h2>
        </div>
        <div class="awards-grid">
          <div class="award-card" v-for="award in awards" :key="award.id">
            <div class="award-image-wrapper">
              <div class="award-image" v-if="award.image">
                <img :src="award.image" :alt="award.title" />
              </div>
              <div class="award-icon" v-else>
                <svg
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                >
                  <circle cx="12" cy="8" r="7" />
                  <polyline points="8.21 13.89 7 23 12 20 17 23 15.79 13.88" />
                </svg>
              </div>
              <div class="award-badge">
                <svg
                  viewBox="0 0 24 24"
                  fill="currentColor"
                  width="20"
                  height="20"
                >
                  <path
                    d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z"
                  />
                </svg>
              </div>
            </div>
            <div class="award-content">
              <h3>{{ award.title }}</h3>
              <div class="award-meta">
                <span v-if="award.organization" class="award-org">
                  <svg
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                    width="14"
                    height="14"
                  >
                    <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />
                    <polyline points="9 22 9 12 15 12 15 22" />
                  </svg>
                  {{ award.organization }}
                </span>
              </div>
              <div class="award-meta">
                <span v-if="award.year" class="award-year">
                  <svg
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                    width="14"
                    height="14"
                  >
                    <rect x="3" y="4" width="18" height="18" rx="2" ry="2" />
                    <line x1="16" y1="2" x2="16" y2="6" />
                    <line x1="8" y1="2" x2="8" y2="6" />
                    <line x1="3" y1="10" x2="21" y2="10" />
                  </svg>
                  {{ award.year }}
                </span>
              </div>
              <p v-if="award.description">{{ award.description }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { onMounted, ref, computed } from "vue";
import { siteCache } from "@/utils/siteCache";
import smartEnergyImage from "@/assets/site/smart-energy.jpg";

const aboutData = ref({});
const heroData = ref({});
const coreValues = ref([]);
const milestones = ref([]);
const reversedMilestones = computed(() => [...milestones.value].reverse());
const teamMembers = ref([]);
const awards = ref([]);
const isLoading = ref(true);

const heroBackgroundStyle = computed(() => {
  if (heroData.value.backgroundImage) {
    const pos = heroData.value.bgPosition || "center";
    return {
      backgroundImage: `url(${heroData.value.backgroundImage})`,
      backgroundSize: "cover",
      backgroundPosition: `center ${pos}`,
    };
  }
  return {};
});

const loadData = async () => {
  try {
    const [about, hero, values, ms, awardData] = await Promise.all([
      siteCache.getAbout(),
      siteCache.getAboutHero(),
      siteCache.getCoreValues(),
      siteCache.getMilestones(),
      siteCache.getAwards(),
    ]);
    aboutData.value = about;
    heroData.value = hero;
    coreValues.value = values;
    milestones.value = ms;
    teamMembers.value = about.teamMembers || [];
    awards.value = awardData;
  } catch (e) {
    console.error("加载关于我们数据失败", e);
  } finally {
    isLoading.value = false;
  }
};

onMounted(() => {
  window.scrollTo(0, 0);
  loadData();
});
</script>

<style scoped>
.about-page {
  padding-top: 0;
}

/* Hero */
.hero {
  position: relative;
  min-height: 600px;
  padding-top: 120px;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  background: linear-gradient(
    160deg,
    #0a1628 0%,
    #132742 30%,
    #1a3a5c 60%,
    #0f2440 100%
  );
  overflow: hidden;
}

.hero::before {
  content: "";
  position: absolute;
  inset: 0;
  background: radial-gradient(
      ellipse 70% 50% at 40% 30%,
      rgba(0, 168, 89, 0.12) 0%,
      transparent 55%
    ),
    radial-gradient(
      ellipse 50% 40% at 70% 70%,
      rgba(0, 168, 89, 0.07) 0%,
      transparent 50%
    ),
    radial-gradient(
      ellipse 40% 30% at 50% 50%,
      rgba(0, 168, 89, 0.04) 0%,
      transparent 45%
    );
  z-index: 0;
}

.hero-overlay {
  position: absolute;
  inset: 0;
  background: radial-gradient(
    ellipse at 60% 40%,
    rgba(0, 168, 89, 0.12) 0%,
    transparent 60%
  );
}

.hero-bg-grid {
  position: absolute;
  inset: 0;
  background-image: linear-gradient(
      rgba(255, 255, 255, 0.025) 1px,
      transparent 1px
    ),
    linear-gradient(90deg, rgba(255, 255, 255, 0.025) 1px, transparent 1px);
  background-size: 60px 60px;
  mask-image: radial-gradient(
    ellipse 70% 60% at 50% 50%,
    black 30%,
    transparent 70%
  );
}

.hero-content {
  position: relative;
  z-index: 1;
  text-align: center;
  max-width: 800px;
  margin: 0 auto;
  padding: 1rem 1rem;
  color: white;
  animation: fadeInUp 0.5s ease;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.hero-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 300px;
}

.loading-spinner {
  width: 48px;
  height: 48px;
  border: 3px solid rgba(255, 255, 255, 0.2);
  border-top-color: #33d17a;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.hero-content h1 {
  font-size: clamp(2rem, 5vw, 3rem);
  font-weight: 800;
  margin-bottom: 1.5rem;
  line-height: 1.2;
  background: linear-gradient(180deg, #ffffff 0%, #c8d6e5 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-description {
  font-size: 1.1rem;
  color: rgba(255, 255, 255, 0.75);
  line-height: 1.8;
  max-width: 650px;
  margin: 0 auto;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1.5rem;
}

/* About Detail */
.about-detail {
  padding: 6rem 0;
  background: #f7fafc;
}

.about-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 5rem;
  align-items: center;
}

.about-text {
  color: #1a202c;
}

.section-subtitle {
  display: inline-block;
  color: #00a859;
  font-weight: 700;
  font-size: 0.85rem;
  text-transform: uppercase;
  letter-spacing: 2px;
  margin-bottom: 0.75rem;
}

.about-text h2 {
  font-size: 2.2rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
  color: #1a365d;
  line-height: 1.3;
}

.about-text p {
  color: #4a5568;
  line-height: 1.85;
  font-size: 1rem;
  white-space: pre-wrap;
}

.about-mission {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-top: 2rem;
  padding: 1rem 1.25rem;
  background: white;
  border-radius: 12px;
  border-left: 4px solid #00a859;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
}

.about-mission svg {
  color: #00a859;
  flex-shrink: 0;
}

.about-mission span {
  color: #1a365d;
  font-weight: 600;
  font-size: 0.95rem;
  line-height: 1.5;
  white-space: pre-wrap;
}

.about-vision {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-top: 1rem;
  padding: 1rem 1.25rem;
  background: white;
  border-radius: 12px;
  border-left: 4px solid #1a365d;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
}

.about-vision svg {
  color: #1a365d;
  flex-shrink: 0;
}

.about-vision span {
  color: #1a365d;
  font-weight: 600;
  font-size: 0.95rem;
  line-height: 1.5;
  white-space: pre-wrap;
}

.about-image {
  position: relative;
}

.about-image img {
  width: 100%;
  height: 420px;
  object-fit: cover;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.12);
  transition: transform 0.5s ease;
}

.about-image:hover img {
  transform: scale(1.02);
}

.image-accent {
  position: absolute;
  bottom: -20px;
  right: -20px;
  width: 180px;
  height: 180px;
  background: linear-gradient(135deg, #00a859, #33d17a);
  border-radius: 20px;
  z-index: -1;
  opacity: 0.12;
}

.about-image::after {
  content: "";
  position: absolute;
  top: -16px;
  left: -16px;
  width: 100px;
  height: 100px;
  border-top: 4px solid #00a859;
  border-left: 4px solid #00a859;
  border-radius: 8px 0 0 0;
  opacity: 0.3;
  z-index: -1;
}

/* Core Values */
.core-values {
  padding: 6rem 0;
  background: #f7fafc;
}

.values-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.5rem;
}

.value-card {
  text-align: center;
  padding: 2.5rem 1.5rem;
  background: white;
  border-radius: 16px;
  border: 1px solid #e2e8f0;
  transition: all 0.35s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.value-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.1);
  border-color: rgba(0, 168, 89, 0.3);
}

.value-icon {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  display: block;
}

.value-card h3 {
  color: #1a365d;
  margin-bottom: 0.75rem;
  font-size: 1.1rem;
  font-weight: 700;
}

.value-card p {
  color: #718096;
  font-size: 0.9rem;
  line-height: 1.6;
  white-space: pre-wrap;
}

/* Team */
.team-section {
  padding: 6rem 0;
  background: white;
}

.team-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 2rem;
}

.team-card {
  background: #f7fafc;
  border-radius: 16px;
  padding: 2.5rem 1.5rem;
  text-align: center;
  border: 1px solid #e2e8f0;
  transition: all 0.35s ease;
}

.team-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.1);
  border-color: rgba(0, 168, 89, 0.2);
}

.team-avatar {
  width: 88px;
  height: 88px;
  border-radius: 50%;
  overflow: hidden;
  margin: 0 auto 1.25rem;
  background: linear-gradient(135deg, #e8f5e9, #c8e6c9);
  display: flex;
  align-items: center;
  justify-content: center;
  border: 3px solid white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.team-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  font-size: 2rem;
  color: #00a859;
  font-weight: 700;
}

.team-card h3 {
  color: #1a365d;
  margin-bottom: 0.5rem;
  font-size: 1.1rem;
}

.team-position {
  color: #00a859;
  font-weight: 600;
  margin-bottom: 0.75rem;
  font-size: 0.85rem;
}

.team-bio {
  color: #718096;
  font-size: 0.85rem;
  line-height: 1.6;
  white-space: pre-wrap;
}

/* Development History */
.development-history {
  padding: 6rem 0;
  background: #f7fafc;
}

.timeline {
  position: relative;
  max-width: 800px;
  margin: 0 auto;
  padding: 0 1rem;
}

.timeline-line {
  position: absolute;
  left: 50%;
  top: 0;
  bottom: 0;
  width: 2px;
  background: linear-gradient(180deg, #00a859, #33d17a, #00a859);
  transform: translateX(-50%);
  border-radius: 1px;
}

.timeline-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 3rem;
  position: relative;
  gap: 2rem;
}

.timeline-item:last-child {
  margin-bottom: 0;
}

.timeline-item.left {
  flex-direction: row;
  padding-right: calc(50% + 2rem);
}

.timeline-item.right {
  flex-direction: row-reverse;
  padding-left: calc(50% + 2rem);
}

.timeline-dot {
  position: absolute;
  left: 50%;
  top: 0;
  transform: translateX(-50%);
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, #00a859, #008f4d);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 700;
  font-size: 0.85rem;
  border: 3px solid #f7fafc;
  box-shadow: 0 0 0 4px rgba(0, 168, 89, 0.2);
  z-index: 1;
}

.timeline-content {
  background: white;
  padding: 1.5rem;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
  transition: all 0.3s ease;
  flex: 1;
}

.timeline-content:hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  border-color: rgba(0, 168, 89, 0.2);
}

.timeline-title {
  display: block;
  color: #2c3e50;
  font-weight: 700;
  font-size: 1.2rem;
  margin-bottom: 0.5rem;
}

.timeline-content p {
  color: #718096;
  line-height: 1.6;
  font-size: 0.95rem;
  white-space: pre-wrap;
}

/* Awards Section */
.awards-section {
  padding: 6rem 0;
  background: linear-gradient(180deg, #f8fafc 0%, #ffffff 100%);
}

.awards-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 2rem;
}

.award-card {
  background: white;
  border-radius: 20px;
  border: 1px solid #e8ecf1;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
}

.award-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 60px rgba(0, 168, 89, 0.12);
  border-color: rgba(0, 168, 89, 0.3);
}

.award-image-wrapper {
  position: relative;
  overflow: hidden;
}

.award-image {
  width: 100%;
  height: 260px;
  overflow: hidden;
  background: linear-gradient(135deg, #f0f4f8 0%, #e8ecf1 100%);
}

.award-image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  padding: 8px;
  transition: transform 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.award-card:hover .award-image img {
  transform: scale(1.08);
}

.award-icon {
  width: 100%;
  height: 260px;
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 50%, #a5d6a7 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.award-icon::before {
  content: "";
  position: absolute;
  inset: 0;
  background: radial-gradient(
    circle at 30% 40%,
    rgba(255, 255, 255, 0.4) 0%,
    transparent 60%
  );
}

.award-icon svg {
  width: 72px;
  height: 72px;
  color: #00a859;
  position: relative;
  z-index: 1;
  filter: drop-shadow(0 4px 8px rgba(0, 168, 89, 0.2));
}

.award-badge {
  position: absolute;
  top: 16px;
  right: 16px;
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #00a859 0%, #33d17a 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 4px 12px rgba(0, 168, 89, 0.3);
  z-index: 2;
}

.award-badge svg {
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

.award-content {
  padding: 1.75rem;
}

.award-content h3 {
  color: #1a365d;
  font-size: 1.15rem;
  font-weight: 700;
  margin-bottom: 1rem;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.award-meta {
  display: flex;
  gap: 1.25rem;
  margin-bottom: 0.75rem;
  flex-wrap: wrap;
  align-items: center;
}

.award-org {
  color: #00a859;
  font-size: 0.85rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 0.4rem;
}

.award-org svg {
  flex-shrink: 0;
  color: #00a859;
}

.award-year {
  color: #718096;
  font-size: 0.85rem;
  display: flex;
  align-items: center;
  gap: 0.4rem;
}

.award-year svg {
  flex-shrink: 0;
  color: #a0aec0;
}

.award-content p {
  color: #718096;
  font-size: 0.9rem;
  line-height: 1.7;
  margin-top: 0.75rem;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  white-space: pre-wrap;
}

@media (max-width: 768px) {
  .hero {
    min-height: 400px;
  }
  .hero-content h1 {
    font-size: 1.6rem;
  }
  .hero-description {
    font-size: 0.95rem;
  }
  .about-grid {
    grid-template-columns: 1fr;
    gap: 2rem;
  }
  .about-image img {
    width: 100%;
    height: 280px !important;
    object-fit: cover;
  }
  .about-detail {
    padding: 4rem 0;
  }
  .core-values,
  .team-section,
  .development-history,
  .awards-section {
    padding: 4rem 0;
  }
  .section-header h2 {
    font-size: 1.6rem;
  }
  .awards-grid {
    grid-template-columns: 1fr;
  }
}
</style>