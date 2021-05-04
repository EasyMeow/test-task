//package com.github.easymeow.artist;
//
//import com.github.easymeow.artist.entity.Artist;
//import org.junit.jupiter.api.Test;
//
//class GenericsTest {
//
//	@Test
//	void recursiveGenerics() {
//
//		Artist artist = new ArtistBuilder<>()
//				.name("valya")
//				.age((byte) 5)
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
//	private static class CustomArtistBuilder<BUILDER extends CustomArtistBuilder<BUILDER>> extends ArtistBuilder<BUILDER> {
//
//		public BUILDER superAge(byte age) {
//			age(age);
//			name(getName() + age);
//			return (BUILDER) this;
//		}
//	}
//
//	private static class ArtistBuilder<T extends ArtistBuilder<T>> {
//		private String name;
//		private byte age;
//
//		public String getName() {
//			return name;
//		}
//
//		public byte getAge() {
//			return age;
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
//			Artist artist = new Artist(name);
////			artist.setAge(age);
//			return artist;
//		}
//
//	}
//}
