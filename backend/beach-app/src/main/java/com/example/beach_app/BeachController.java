package com.example.beach_app;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class BeachController {
    @GetMapping(value = "/beach", produces = MediaType.TEXT_HTML_VALUE)
    public String beachHome() {
        return """
            <!doctype html>
            <html>
            <head>
              <meta charset="utf-8"/>
              <meta name="viewport" content="width=device-width, initial-scale=1"/>
              <title>🏖️ Beach Paradise</title>
              <style>
                * { margin: 0; padding: 0; box-sizing: border-box; }
                
                body {
                  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                  color: #2c3e50;
                  min-height: 100vh;
                }
                
                @keyframes wave {
                  0%, 100% { transform: translateY(0); }
                  50% { transform: translateY(-10px); }
                }
                
                @keyframes fadeIn {
                  from { opacity: 0; transform: translateY(20px); }
                  to { opacity: 1; transform: translateY(0); }
                }
                
                @keyframes float {
                  0%, 100% { transform: translateY(0px) rotate(0deg); }
                  50% { transform: translateY(-20px) rotate(5deg); }
                }
                
                header {
                  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                  padding: 60px 20px;
                  text-align: center;
                  color: white;
                  position: relative;
                  overflow: hidden;
                  box-shadow: 0 10px 40px rgba(0,0,0,0.3);
                }
                
                header::before {
                  content: '🌊';
                  position: absolute;
                  font-size: 120px;
                  opacity: 0.1;
                  top: 20px;
                  left: 50px;
                  animation: float 6s ease-in-out infinite;
                }
                
                header::after {
                  content: '☀️';
                  position: absolute;
                  font-size: 100px;
                  opacity: 0.15;
                  top: 30px;
                  right: 80px;
                  animation: float 8s ease-in-out infinite;
                }
                
                h1 {
                  font-size: 3.5em;
                  margin-bottom: 15px;
                  text-shadow: 3px 3px 6px rgba(0,0,0,0.3);
                  animation: fadeIn 1s ease-out;
                }
                
                .subtitle {
                  font-size: 1.3em;
                  opacity: 0.95;
                  font-style: italic;
                  animation: fadeIn 1.5s ease-out;
                }
                
                .wrap {
                  max-width: 1200px;
                  margin: -40px auto 0;
                  padding: 0 20px 40px;
                  position: relative;
                  z-index: 10;
                }
                
                .card {
                  background: white;
                  border-radius: 24px;
                  padding: 35px;
                  box-shadow: 0 15px 50px rgba(0,0,0,0.15);
                  margin: 30px 0;
                  animation: fadeIn 1s ease-out;
                  transition: transform 0.3s ease, box-shadow 0.3s ease;
                }
                
                .card:hover {
                  transform: translateY(-8px);
                  box-shadow: 0 20px 60px rgba(0,0,0,0.25);
                }
                
                .quote-card {
                  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
                  color: white;
                  text-align: center;
                  padding: 45px;
                  font-size: 1.3em;
                  font-style: italic;
                  position: relative;
                }
                
                .quote-card::before {
                  content: '"';
                  font-size: 80px;
                  position: absolute;
                  top: 10px;
                  left: 25px;
                  opacity: 0.3;
                }
                
                .poem-card {
                  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
                  color: white;
                  padding: 40px;
                }
                
                .poem-text {
                  font-size: 1.15em;
                  line-height: 1.8;
                  text-align: center;
                  font-style: italic;
                }
                
                .btn {
                  display: inline-block;
                  padding: 14px 28px;
                  border-radius: 50px;
                  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                  color: white;
                  text-decoration: none;
                  margin: 8px;
                  font-weight: bold;
                  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
                  transition: all 0.3s ease;
                  font-size: 1.05em;
                }
                
                .btn:hover {
                  transform: translateY(-3px);
                  box-shadow: 0 12px 30px rgba(102, 126, 234, 0.6);
                }
                
                .btn-secondary {
                  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
                  box-shadow: 0 8px 20px rgba(245, 87, 108, 0.4);
                }
                
                .btn-secondary:hover {
                  box-shadow: 0 12px 30px rgba(245, 87, 108, 0.6);
                }
                
                .grid {
                  display: grid;
                  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
                  gap: 25px;
                  margin-top: 25px;
                }
                
                .gallery-item {
                  position: relative;
                  overflow: hidden;
                  border-radius: 20px;
                  box-shadow: 0 8px 25px rgba(0,0,0,0.2);
                  transition: transform 0.3s ease;
                }
                
                .gallery-item:hover {
                  transform: scale(1.05);
                }
                
                .gallery-item img {
                  width: 100%;
                  height: 250px;
                  object-fit: cover;
                  display: block;
                  transition: transform 0.5s ease;
                }
                
                .gallery-item:hover img {
                  transform: scale(1.1);
                }
                
                .gallery-overlay {
                  position: absolute;
                  bottom: 0;
                  left: 0;
                  right: 0;
                  background: linear-gradient(to top, rgba(0,0,0,0.8), transparent);
                  color: white;
                  padding: 20px;
                  transform: translateY(100%);
                  transition: transform 0.3s ease;
                }
                
                .gallery-item:hover .gallery-overlay {
                  transform: translateY(0);
                }
                
                h2 {
                  color: #667eea;
                  margin-bottom: 20px;
                  font-size: 2em;
                  display: flex;
                  align-items: center;
                  gap: 12px;
                }
                
                .emoji {
                  font-size: 1.2em;
                  display: inline-block;
                  animation: wave 2s ease-in-out infinite;
                }
                
                footer {
                  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                  padding: 30px;
                  text-align: center;
                  color: white;
                  margin-top: 50px;
                  font-size: 1.1em;
                }
                
                .features {
                  display: grid;
                  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                  gap: 20px;
                  margin-top: 25px;
                }
                
                .feature {
                  text-align: center;
                  padding: 25px;
                  background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
                  border-radius: 18px;
                  transition: transform 0.3s ease;
                }
                
                .feature:hover {
                  transform: translateY(-5px);
                }
                
                .feature-icon {
                  font-size: 3.5em;
                  margin-bottom: 12px;
                  display: block;
                }
                
                .author {
                  margin-top: 15px;
                  font-size: 0.95em;
                  opacity: 0.9;
                }
              </style>
            </head>
            <body>
              <header>
                <h1>🏖️ Beach Paradise</h1>
                <p class="subtitle">Where the ocean meets tranquility</p>
              </header>
              
              <div class="wrap">
                <div class="card quote-card">
                  <p>To escape and sit quietly on the beach — that's my idea of paradise.</p>
                  <p class="author">— Emilia Wickstead</p>
                </div>
                
                <div class="card">
                  <h2><span class="emoji">🌊</span> Welcome to Your Coastal Escape</h2>
                  <p style="font-size: 1.15em; line-height: 1.7; margin-bottom: 25px;">
                    Immerse yourself in the soothing rhythm of waves, the warmth of golden sunshine, 
                    and the endless beauty of pristine beaches. Your journey to paradise begins here.
                  </p>
                  <div style="text-align: center;">
                    <a class="btn" href="/beach/facts">🌊 Beach Facts</a>
                    <a class="btn btn-secondary" href="/beach/places">🏝️ Top Places</a>
                    <a class="btn" href="/beach/quotes">💭 Inspirations</a>
                  </div>
                </div>
                
                <div class="card">
                  <h2><span class="emoji">🎨</span> Stunning Beach Gallery</h2>
                  <div class="grid">
                    <div class="gallery-item">
                      <img src="https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=1200&q=80" alt="Tropical Paradise"/>
                      <div class="gallery-overlay">
                        <h3>Tropical Paradise</h3>
                        <p>Crystal clear waters</p>
                      </div>
                    </div>
                    <div class="gallery-item">
                      <img src="https://images.unsplash.com/photo-1559827260-dc66d52bef19?auto=format&fit=crop&w=1200&q=80" alt="Sunset Beach"/>
                      <div class="gallery-overlay">
                        <h3>Golden Sunset</h3>
                        <p>Nature's masterpiece</p>
                      </div>
                    </div>
                    <div class="gallery-item">
                      <img src="https://images.unsplash.com/photo-1473496169904-658ba7c44d8a?auto=format&fit=crop&w=1200&q=80" alt="Palm Paradise"/>
                      <div class="gallery-overlay">
                        <h3>Palm Paradise</h3>
                        <p>Swaying in the breeze</p>
                      </div>
                    </div>
                    <div class="gallery-item">
                      <img src="https://images.unsplash.com/photo-1519046904884-53103b34b206?auto=format&fit=crop&w=1200&q=80" alt="Ocean Waves"/>
                      <div class="gallery-overlay">
                        <h3>Ocean Waves</h3>
                        <p>Rhythmic serenity</p>
                      </div>
                    </div>
                    <div class="gallery-item">
                      <img src="https://images.unsplash.com/photo-1471922694854-ff1b63b20054?auto=format&fit=crop&w=1200&q=80" alt="Coastal Beauty"/>
                      <div class="gallery-overlay">
                        <h3>Coastal Beauty</h3>
                        <p>Where earth meets sea</p>
                      </div>
                    </div>
                    <div class="gallery-item">
                      <img src="https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?auto=format&fit=crop&w=1200&q=80" alt="Starfish Beach"/>
                      <div class="gallery-overlay">
                        <h3>Marine Wonder</h3>
                        <p>Hidden treasures</p>
                      </div>
                    </div>
                  </div>
                </div>
                
                <div class="card poem-card">
                  <h2 style="color: white; text-align: center;">🌅 Ocean's Whisper</h2>
                  <div class="poem-text">
                    <p>The ocean whispers to the shore,</p>
                    <p>Of distant lands and ancient lore.</p>
                    <p>The waves dance in the morning light,</p>
                    <p>As seabirds soar in graceful flight.</p>
                    <br/>
                    <p>The sand beneath my wandering feet,</p>
                    <p>Where earth and endless waters meet.</p>
                    <p>The sunset paints the sky ablaze,</p>
                    <p>As peace descends to end the days.</p>
                  </div>
                </div>
                
                <div class="card">
                  <h2><span class="emoji">✨</span> Beach Experience</h2>
                  <div class="features">
                    <div class="feature">
                      <span class="feature-icon">🌅</span>
                      <h3>Sunrise Magic</h3>
                      <p>Start your day with breathtaking dawn colors</p>
                    </div>
                    <div class="feature">
                      <span class="feature-icon">🏄</span>
                      <h3>Adventure</h3>
                      <p>Surf the waves and feel alive</p>
                    </div>
                    <div class="feature">
                      <span class="feature-icon">🧘</span>
                      <h3>Tranquility</h3>
                      <p>Find peace in ocean's rhythm</p>
                    </div>
                    <div class="feature">
                      <span class="feature-icon">🐚</span>
                      <h3>Treasures</h3>
                      <p>Discover shells and sea wonders</p>
                    </div>
                  </div>
                </div>
              </div>
              
              <footer>
                <p>🏖️ Beach Paradise • Spring Boot • Powered by Ocean Dreams</p>
                <p style="margin-top: 10px; opacity: 0.9;">Let the waves wash your worries away</p>
              </footer>
            </body>
            </html>
            """;
    }
    
    @GetMapping(value = "/beach/facts", produces = MediaType.TEXT_HTML_VALUE)
    public String beachFacts() {
        return """
            <!doctype html>
            <html>
            <head>
              <meta charset="utf-8"/>
              <meta name="viewport" content="width=device-width, initial-scale=1"/>
              <title>🌊 Beach Facts</title>
              <style>
                * { margin: 0; padding: 0; box-sizing: border-box; }
                body {
                  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                  min-height: 100vh;
                  padding: 40px 20px;
                }
                .container {
                  max-width: 900px;
                  margin: 0 auto;
                }
                .header {
                  text-align: center;
                  color: white;
                  margin-bottom: 40px;
                }
                .header h1 {
                  font-size: 3em;
                  margin-bottom: 10px;
                  text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
                }
                .fact-card {
                  background: white;
                  border-radius: 20px;
                  padding: 30px;
                  margin: 25px 0;
                  box-shadow: 0 10px 40px rgba(0,0,0,0.2);
                  transition: transform 0.3s ease;
                }
                .fact-card:hover {
                  transform: translateY(-5px);
                }
                .fact-icon {
                  font-size: 3em;
                  margin-bottom: 15px;
                  display: block;
                }
                .fact-card h3 {
                  color: #667eea;
                  margin-bottom: 12px;
                  font-size: 1.5em;
                }
                .fact-card p {
                  line-height: 1.7;
                  font-size: 1.1em;
                  color: #555;
                }
                .btn-back {
                  display: inline-block;
                  padding: 15px 35px;
                  background: white;
                  color: #667eea;
                  text-decoration: none;
                  border-radius: 50px;
                  font-weight: bold;
                  box-shadow: 0 8px 20px rgba(255,255,255,0.3);
                  transition: all 0.3s ease;
                  margin-top: 30px;
                  font-size: 1.1em;
                }
                .btn-back:hover {
                  transform: translateY(-3px);
                  box-shadow: 0 12px 30px rgba(255,255,255,0.5);
                }
                .quote-box {
                  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
                  color: white;
                  padding: 30px;
                  border-radius: 20px;
                  text-align: center;
                  font-style: italic;
                  font-size: 1.2em;
                  margin: 30px 0;
                }
              </style>
            </head>
            <body>
              <div class="container">
                <div class="header">
                  <h1>🌊 Fascinating Beach Facts</h1>
                  <p style="font-size: 1.2em; margin-top: 10px;">Discover the wonders of our coastal world</p>
                </div>
                
                <div class="fact-card">
                  <span class="fact-icon">🌊</span>
                  <h3>The Power of Waves</h3>
                  <p>Waves are primarily formed by wind transferring energy to the water surface. The stronger and longer the wind blows, the larger the waves become. Some waves can travel thousands of miles across the ocean before reaching the shore!</p>
                </div>
                
                <div class="fact-card">
                  <span class="fact-icon">🏖️</span>
                  <h3>Rainbow of Sands</h3>
                  <p>Beach sand comes in many colors—from white to pink to black! The color depends on the local geology, minerals, shells, and coral. White sand beaches often contain quartz and coral fragments, while black sand beaches are formed from volcanic rocks.</p>
                </div>
                
                <div class="fact-card">
                  <span class="fact-icon">🛡️</span>
                  <h3>Natural Protectors</h3>
                  <p>Coastal ecosystems like mangroves, coral reefs, and dunes act as natural barriers protecting inland areas from storms, erosion, and tsunamis. They absorb wave energy and reduce the impact of extreme weather events.</p>
                </div>
                
                <div class="fact-card">
                  <span class="fact-icon">🌙</span>
                  <h3>Tidal Mysteries</h3>
                  <p>The moon's gravitational pull causes ocean tides, creating high and low tides twice daily. The highest tides, called "spring tides," occur during full and new moons when the sun, moon, and Earth align.</p>
                </div>
                
                <div class="fact-card">
                  <span class="fact-icon">🐚</span>
                  <h3>Ocean Symphony</h3>
                  <p>When you hold a seashell to your ear, you're not actually hearing the ocean! You're hearing the ambient noise around you resonating within the shell's cavity, creating that familiar ocean-like sound.</p>
                </div>
                
                <div class="fact-card">
                  <span class="fact-icon">🌍</span>
                  <h3>Coastal Biodiversity</h3>
                  <p>Despite covering less than 10% of the ocean, coastal areas support approximately 90% of all marine species. These ecosystems are among the most productive and diverse habitats on Earth.</p>
                </div>
                
                <div class="quote-box">
                  "The voice of the sea speaks to the soul." — Kate Chopin
                </div>
                
                <div style="text-align: center;">
                  <a class="btn-back" href="/beach">⬅ Back to Paradise</a>
                </div>
              </div>
            </body>
            </html>
            """;
    }
    
    @GetMapping(value = "/beach/places", produces = MediaType.TEXT_HTML_VALUE)
    public String beachPlaces() {
        return """
            <!doctype html>
            <html>
            <head>
              <meta charset="utf-8"/>
              <meta name="viewport" content="width=device-width, initial-scale=1"/>
              <title>🏝️ Top Beach Destinations</title>
              <style>
                * { margin: 0; padding: 0; box-sizing: border-box; }
                body {
                  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                  min-height: 100vh;
                  padding: 40px 20px;
                }
                .container {
                  max-width: 1000px;
                  margin: 0 auto;
                }
                .header {
                  text-align: center;
                  color: white;
                  margin-bottom: 40px;
                }
                .header h1 {
                  font-size: 3em;
                  margin-bottom: 10px;
                  text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
                }
                .place-card {
                  background: white;
                  border-radius: 24px;
                  overflow: hidden;
                  margin: 30px 0;
                  box-shadow: 0 15px 50px rgba(0,0,0,0.25);
                  transition: transform 0.3s ease;
                }
                .place-card:hover {
                  transform: translateY(-8px);
                }
                .place-image {
                  width: 100%;
                  height: 300px;
                  object-fit: cover;
                }
                .place-content {
                  padding: 30px;
                }
                .place-number {
                  display: inline-block;
                  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                  color: white;
                  width: 50px;
                  height: 50px;
                  border-radius: 50%;
                  text-align: center;
                  line-height: 50px;
                  font-size: 1.5em;
                  font-weight: bold;
                  margin-bottom: 15px;
                }
                .place-card h3 {
                  color: #667eea;
                  font-size: 2em;
                  margin-bottom: 15px;
                }
                .place-card p {
                  line-height: 1.7;
                  font-size: 1.1em;
                  color: #555;
                  margin-bottom: 15px;
                }
                .highlights {
                  display: flex;
                  gap: 15px;
                  flex-wrap: wrap;
                  margin-top: 15px;
                }
                .highlight-tag {
                  background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
                  padding: 8px 16px;
                  border-radius: 20px;
                  font-size: 0.95em;
                  font-weight: 500;
                }
                .btn-back {
                  display: inline-block;
                  padding: 15px 35px;
                  background: white;
                  color: #667eea;
                  text-decoration: none;
                  border-radius: 50px;
                  font-weight: bold;
                  box-shadow: 0 8px 20px rgba(255,255,255,0.3);
                  transition: all 0.3s ease;
                  margin-top: 30px;
                  font-size: 1.1em;
                }
                .btn-back:hover {
                  transform: translateY(-3px);
                  box-shadow: 0 12px 30px rgba(255,255,255,0.5);
                }
                .quote-box {
                  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
                  color: white;
                  padding: 30px;
                  border-radius: 20px;
                  text-align: center;
                  font-style: italic;
                  font-size: 1.2em;
                  margin: 30px 0;
                }
              </style>
            </head>
            <body>
              <div class="container">
                <div class="header">
                  <h1>🏝️ World's Most Beautiful Beaches</h1>
                  <p style="font-size: 1.2em; margin-top: 10px;">Paradise destinations that will take your breath away</p>
                </div>
                
                <div class="place-card">
                  <img class="place-image" src="https://images.unsplash.com/photo-1514282401047-d79a71a590e8?auto=format&fit=crop&w=1200&q=80" alt="Maldives"/>
                  <div class="place-content">
                    <span class="place-number">1</span>
                    <h3>🏝️ Maldives</h3>
                    <p>A tropical paradise where luxury meets nature. The Maldives boasts crystal-clear turquoise lagoons, pristine white sand beaches, and spectacular coral reefs. Each resort occupies its own private island, offering unparalleled privacy and breathtaking overwater bungalows.</p>
                    <div class="highlights">
                      <span class="highlight-tag">🤿 World-class diving</span>
                      <span class="highlight-tag">🏖️ Private islands</span>
                      <span class="highlight-tag">🐠 Marine life</span>
                      <span class="highlight-tag">🌅 Stunning sunsets</span>
                    </div>
                  </div>
                </div>
                
                <div class="place-card">
                  <img class="place-image" src="https://images.unsplash.com/photo-1537996194471-e657df975ab4?auto=format&fit=crop&w=1200&q=80" alt="Bali"/>
                  <div class="place-content">
                    <span class="place-number">2</span>
                    <h3>🌺 Bali, Indonesia</h3>
                    <p>The Island of Gods combines spiritual culture with natural beauty. Famous for its world-class surf breaks, dramatic cliff-side beaches, and legendary sunsets. Bali offers a perfect blend of adventure, relaxation, and cultural immersion with its ancient temples and vibrant traditions.</p>
                    <div class="highlights">
                      <span class="highlight-tag">🏄 Surfing paradise</span>
                      <span class="highlight-tag">🛕 Ancient temples</span>
                      <span class="highlight-tag">🌋 Volcanic beaches</span>
                      <span class="highlight-tag">🍜 Amazing cuisine</span>
                    </div>
                  </div>
                </div>
                
                <div class="place-card">
                  <img class="place-image" src="https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?auto=format&fit=crop&w=1200&q=80" alt="Goa"/>
                  <div class="place-content">
                    <span class="place-number">3</span>
                    <h3>🥥 Goa, India</h3>
                    <p>India's beach capital offers endless stretches of golden sand along the Arabian Sea. Known for its laid-back vibe, Portuguese heritage, beach shacks serving fresh seafood, and vibrant nightlife. Goa perfectly balances party culture with peaceful beach walks and spiritual retreats.</p>
                    <div class="highlights">
                      <span class="highlight-tag">🎉 Beach parties</span>
                      <span class="highlight-tag">🦞 Fresh seafood</span>
                      <span class="highlight-tag">🏛️ Portuguese forts</span>
                      <span class="highlight-tag">🥾 Coastal hikes</span>
                    </div>
                  </div>
                </div>
                
                <div class="place-card">
                  <img class="place-image" src="https://images.unsplash.com/photo-1559827260-dc66d52bef19?auto=format&fit=crop&w=1200&q=80" alt="Santorini"/>
                  <div class="place-content">
                    <span class="place-number">4</span>
                    <h3>🇬🇷 Santorini, Greece</h3>
                    <p>Iconic white-washed buildings perched on volcanic cliffs overlooking the deep blue Aegean Sea. Santorini's unique red and black sand beaches are framed by dramatic caldera views. The island offers romance, stunning architecture, and some of the world's most photographed sunsets.</p>
                    <div class="highlights">
                      <span class="highlight-tag">📸 Iconic views</span>
                      <span class="highlight-tag">🍷 Local wineries</span>
                      <span class="highlight-tag">🌋 Volcanic beaches</span>
                      <span class="highlight-tag">💑 Romantic getaway</span>
                    </div>
                  </div>
                </div>
                
                <div class="place-card">
                  <img class="place-image" src="https://images.unsplash.com/photo-1506929562872-bb421503ef21?auto=format&fit=crop&w=1200&q=80" alt="Seychelles"/>
                  <div class="place-content">
                    <span class="place-number">5</span>
                    <h3>🏖️ Seychelles</h3>
                    <p>An archipelago of 115 islands in the Indian Ocean, Seychelles is home to some of the world's most beautiful beaches. Giant granite boulders, powdery white sand, and lush tropical vegetation create a dreamlike landscape. The islands are a haven for rare wildlife and marine conservation.</p>
                    <div class="highlights">
                      <span class="highlight-tag">🐢 Giant tortoises</span>
                      <span class="highlight-tag">🪨 Unique rock formations</span>
                      <span class="highlight-tag">🌴 Pristine nature</span>
                      <span class="highlight-tag">🏊 Crystal waters</span>
                    </div>
                  </div>
                </div>
                
                <div class="quote-box">
                  "The cure for anything is salt water: sweat, tears, or the sea." — Isak Dinesen
                </div>
                
                <div style="text-align: center;">
                  <a class="btn-back" href="/beach">⬅ Back to Paradise</a>
                </div>
              </div>
            </body>
            </html>
            """;
    }
    
    @GetMapping(value = "/beach/quotes", produces = MediaType.TEXT_HTML_VALUE)
    public String beachQuotes() {
        return """
            <!doctype html>
            <html>
            <head>
              <meta charset="utf-8"/>
              <meta name="viewport" content="width=device-width, initial-scale=1"/>
              <title>💭 Beach Inspirations</title>
              <style>
                * { margin: 0; padding: 0; box-sizing: border-box; }
                body {
                  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                  min-height: 100vh;
                  padding: 40px 20px;
                }
                .container {
                  max-width: 900px;
                  margin: 0 auto;
                }
                .header {
                  text-align: center;
                  color: white;
                  margin-bottom: 40px;
                }
                .header h1 {
                  font-size: 3em;
                  margin-bottom: 10px;
                  text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
                }
                .quote-card {
                  background: white;
                  border-radius: 20px;
                  padding: 40px;
                  margin: 25px 0;
                  box-shadow: 0 10px 40px rgba(0,0,0,0.2);
                  transition: transform 0.3s ease;
                  position: relative;
                  overflow: hidden;
                }
                .quote-card::before {
                  content: '"';
                  position: absolute;
                  font-size: 150px;
                  color: rgba(102, 126, 234, 0.1);
                  top: -20px;
                  left: 20px;
                  font-family: Georgia, serif;
                }
                .quote-card:hover {
                  transform: translateY(-5px);
                }
                .quote-text {
                  font-size: 1.4em;
                  font-style: italic;
                  color: #2c3e50;
                  line-height: 1.6;
                  margin-bottom: 20px;
                  position: relative;
                  z-index: 1;
                }
                .quote-author {
                  text-align: right;
                  color: #667eea;
                  font-weight: bold;
                  font-size: 1.1em;
                }
                .poem-card {
                  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
                  color: white;
                  border-radius: 20px;
                  padding: 40px;
                  margin: 25px 0;
                  box-shadow: 0 10px 40px rgba(0,0,0,0.2);
                }
                .poem-title {
                  font-size: 2em;
                  margin-bottom: 25px;
                  text-align: center;
                }
                .poem-text {
                  font-size: 1.2em;
                  line-height: 2;
                  text-align: center;
                  font-style: italic;
                }
                .btn-back {
                  display: inline-block;
                  padding: 15px 35px;
                  background: white;
                  color: #667eea;
                  text-decoration: none;
                  border-radius: 50px;
                  font-weight: bold;
                  box-shadow: 0 8px 20px rgba(255,255,255,0.3);
                  transition: all 0.3s ease;
                  margin-top: 30px;
                  font-size: 1.1em;
                }
                .btn-back:hover {
                  transform: translateY(-3px);
                  box-shadow: 0 12px 30px rgba(255,255,255,0.5);
                }
              </style>
            </head>
            <body>
              <div class="container">
                <div class="header">
                  <h1>💭 Beach Wisdom & Poetry</h1>
                  <p style="font-size: 1.2em; margin-top: 10px;">Let the ocean's voice inspire your soul</p>
                </div>
                
                <div class="quote-card">
                  <p class="quote-text">The ocean stirs the heart, inspires the imagination and brings eternal joy to the soul.</p>
                  <p class="quote-author">— Robert Wyland</p>
                </div>
                
                <div class="quote-card">
                  <p class="quote-text">To escape and sit quietly on the beach — that's my idea of paradise.</p>
                  <p class="quote-author">— Emilia Wickstead</p>
                </div>
                
                <div class="poem-card">
                  <h2 class="poem-title">🌊 The Sea's Embrace</h2>
                  <div class="poem-text">
                    <p>Where azure waters kiss the golden shore,</p>
                    <p>And gentle waves sing songs of distant lore,</p>
                    <p>The seagulls dance above the foaming crest,</p>
                    <p>While weary souls find peace and blissful rest.</p>
                    <br/>
                    <p>The sun descends in colors bright and bold,</p>
                    <p>Painting the horizon bronze and gold,</p>
                    <p>As stars emerge to guard the tranquil night,</p>
                    <p>The ocean gleams beneath the soft moonlight.</p>
                  </div>
                </div>
                
                <div class="quote-card">
                  <p class="quote-text">The sea, once it casts its spell, holds one in its net of wonder forever.</p>
                  <p class="quote-author">— Jacques Cousteau</p>
                </div>
                
                <div class="quote-card">
                  <p class="quote-text">Why do we love the sea? It is because it has some potent power to make us think things we like to think.</p>
                  <p class="quote-author">— Robert Henri</p>
                </div>
                
                <div class="poem-card">
                  <h2 class="poem-title">🏖️ Footprints in Sand</h2>
                  <div class="poem-text">
                    <p>Leave your footprints in the sand so fine,</p>
                    <p>Where ocean breezes whisper and entwine,</p>
                    <p>Each wave that breaks upon the waiting shore,</p>
                    <p>Erases troubles from the days before.</p>
                    <br/>
                    <p>With shells and treasures scattered all around,</p>
                    <p>In nature's gallery, peace is found,</p>
                    <p>The rhythm of the tide, forever true,</p>
                    <p>Reminds us life begins anew.</p>
                  </div>
                </div>
                
                <div class="quote-card">
                  <p class="quote-text">The voice of the sea speaks to the soul. The touch of the sea is sensuous, enfolding the body in its soft, close embrace.</p>
                  <p class="quote-author">— Kate Chopin</p>
                </div>
                
                <div class="quote-card">
                  <p class="quote-text">Dance with the waves, move with the sea, let the rhythm of the water set your soul free.</p>
                  <p class="quote-author">— Christy Ann Martine</p>
                </div>
                
                <div class="quote-card">
                  <p class="quote-text">The cure for anything is salt water: sweat, tears, or the sea.</p>
                  <p class="quote-author">— Isak Dinesen</p>
                </div>
                
                <div class="poem-card">
                  <h2 class="poem-title">🌅 Dawn at the Beach</h2>
                  <div class="poem-text">
                    <p>When morning breaks on distant ocean blue,</p>
                    <p>The world awakens, fresh with morning dew,</p>
                    <p>The dolphins leap through waves of purest light,</p>
                    <p>Bidding farewell to the peaceful night.</p>
                    <br/>
                    <p>The beach at dawn, a sacred, silent place,</p>
                    <p>Where nature shows its most enchanting face,</p>
                    <p>And all who walk along the water's edge,</p>
                    <p>Find renewal in the ocean's pledge.</p>
                  </div>
                </div>
                
                <div class="quote-card">
                  <p class="quote-text">The ocean is everything I want to be. Beautiful, mysterious, wild, and free.</p>
                  <p class="quote-author">— Unknown</p>
                </div>
                
                <div style="text-align: center;">
                  <a class="btn-back" href="/beach">⬅ Back to Paradise</a>
                </div>
              </div>
            </body>
            </html>
            """;
    }
}
