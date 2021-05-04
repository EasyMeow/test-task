//package com.github.easymeow.artist;
//
//import com.github.easymeow.artist.entity.Artist;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class GenericsWithInstrumentTest {
//
//	static <INSTRUMENT extends Instrument, BUILDER extends ArtistBuilder<INSTRUMENT, BUILDER>> BUILDER getBuilder(Class<INSTRUMENT> instrumentClass) {
//		return (BUILDER) new ArtistBuilder<INSTRUMENT, BUILDER>();
//	}
//
//	@Test
//	void contextLoads() {
//
//		Artist artist = new ArtistBuilder<>()
//				.name("valya")
//				.age((byte) 5)
//				.instrument(new Piano(""))
//				.build();
//
//		artist = getBuilder(Piano.class)
//				.name("valya")
//				.age((byte) 5)
//				.instrument(new Piano(""))
//				.build();
//
//		System.out.println(artist);
//
//
//		Artist artist1 = new CustomArtistBuilder<>()
//				.name("valya")
//				.age((byte) 5)
//				.superAge((byte) 5)
//				.superAge((byte) 5)
//				.build();
//
//	}
//
//	private static class CustomArtistBuilder<INSTRUMENT extends Instrument, T extends GenericsWithInstrumentTest.CustomArtistBuilder<INSTRUMENT, T>> extends ArtistBuilder<INSTRUMENT, T> {
//
//		public T superAge(byte age) {
//			age(age);
//			name(getName() + age);
//			return (T) this;
//		}
//	}
//
//	private static class ArtistBuilder<INSTRUMENT extends Instrument, T extends GenericsWithInstrumentTest.ArtistBuilder<INSTRUMENT, T>> {
//		private String name;
//		private byte age;
//		private INSTRUMENT instrument;
//
//		public String getName() {
//			return name;
//		}
//
//		public byte getAge() {
//			return age;
//		}
//
//		public T instrument(INSTRUMENT instrument) {
//			this.instrument = instrument;
//			return (T) this;
//		}
//
//		public T name(String name) {
//			this.name = name;
//			return (T) this;
//		}
//
//		public T age(byte age) {
//			this.age = age;
//			return (T) this;
//		}
//
//		public Artist build() {
////			Artist<INSTRUMENT> artist = new Artist<INSTRUMENT>(name);
//			Artist artist = new Artist(name);
////			artist.setAge(age);
//			return artist;
//		}
//
//	}
//}
