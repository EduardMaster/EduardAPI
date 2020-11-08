package net.eduard.api.lib.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("unused")
public enum ParticleEffect {
	  HUGE_EXPLOSION(
			    "hugeexplosion"), 
			  LARGE_EXPLODE(
			    "largeexplode"), 
			  FIREWORKS_SPARK(
			    "fireworksSpark"), 
			  BUBBLE(
			    "bubble"), 
			  SUSPEND(
			    "suspend"), 
			  DEPTH_SUSPEND(
			    "depthSuspend"), 
			  TOWN_AURA(
			    "townaura"), 
			  CRIT(
			    "crit"), 
			  MAGIC_CRIT(
			    "magicCrit"), 
			  SMOKE(
			    "smoke"), 
			  MOB_SPELL(
			    "mobSpell"), 
			  MOB_SPELL_AMBIENT(
			    "mobSpellAmbient"), 
			  SPELL(
			    "spell"), 
			  INSTANT_SPELL(
			    "instantSpell"), 
			  WITCH_MAGIC(
			    "witchMagic"), 
			  NOTE(
			    "note"), 
			  PORTAL(
			    "portal"), 
			  ENCHANTMENT_TABLE(
			    "enchantmenttable"), 
			  EXPLODE(
			    "explode"), 
			  FLAME(
			    "flame"), 
			  LAVA(
			    "lava"), 
			  FOOTSTEP(
			    "footstep"), 
			  SPLASH(
			    "splash"), 
			  WAKE(
			    "wake"), 
			  LARGE_SMOKE(
			    "largesmoke"), 
			  CLOUD(
			    "cloud"), 
			  RED_DUST(
			    "reddust"), 
			  SNOWBALL_POOF(
			    "snowballpoof"), 
			  DRIP_WATER(
			    "dripWater"), 
			  DRIP_LAVA(
			    "dripLava"), 
			  SNOW_SHOVEL(
			    "snowshovel"), 
			  SLIME(
			    "slime"), 
			  HEART(
			    "heart"), 
			  ANGRY_VILLAGER(
			    "angryVillager"), 
			  HAPPY_VILLAGER(
			    "happyVillager");

			  private static final double MAX_RANGE = 16.0D;
			  private static Constructor<?> packetPlayOutWorldParticles;
			  private static Method getHandle;
			  private static Field playerConnection;
			  private static Method sendPacket;
			  private static final Map<String, ParticleEffect> NAME_MAP;
			  private final String name;
			  
			  public static double cos(double i)
			  {
			    return Math.cos(i);
			  }

			  public static double sin(double i) {
			    return Math.sin(i);
			  }
			  public static Plugin getPlugin(){
				  return JavaPlugin.getProvidingPlugin(ParticleEffect.class);
			  }

			 

			  static { NAME_MAP = new HashMap<>();

			    for (ParticleEffect p : values())
			      NAME_MAP.put(p.name, p);
			    try {
			      packetPlayOutWorldParticles = Reflections.getConstructor(Reflections.PacketType.PLAY_OUT_WORLD_PARTICLES.getPacket(), new Class[] { String.class, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, 
			        Float.TYPE, Float.TYPE, Integer.TYPE });
			      getHandle = Reflections.getMethod("CraftPlayer", Reflections.SubPackageType.ENTITY, "getHandle", new Class[0]);
			      playerConnection = Reflections.getField("EntityPlayer", Reflections.PackageType.MINECRAFT_SERVER, "playerConnection");
			      sendPacket = Reflections.getMethod(playerConnection.getType(), "sendPacket", new Class[] { Reflections.getClass("Packet", Reflections.PackageType.MINECRAFT_SERVER) });
			    } catch (Exception e) {
			      e.printStackTrace();
			    }
			  }

			  ParticleEffect(String name)
			  {
			    this.name = name;
			  }

			  public String getName()
			  {
			    return this.name;
			  }

			  public static ParticleEffect fromName(String name)
			  {
			    if (name != null)
			      for (Map.Entry<?,?> e : NAME_MAP.entrySet())
			        if (((String)e.getKey()).equalsIgnoreCase(name))
			          return (ParticleEffect)e.getValue();
			    return null;
			  }

	private static final class PacketInstantiationException extends RuntimeException
	  {
	    private static final long serialVersionUID = 3203085387160737484L;

	    public PacketInstantiationException(String message)
	    {
	    	super(message);
	    }

	    public PacketInstantiationException(String message, Throwable cause)
	    {
	      super(message);
	    }
	  }

	  private static final class PacketSendingException extends RuntimeException
	  {
	    private static final long serialVersionUID = 3203085387160737484L;

	    public PacketSendingException(String message, Throwable cause)
	    {
	      super();
	    }
	  }
	 private static List<Player> getPlayers(Location center, double range)
	  {
	    List<Player> players = new ArrayList<>();
	    String name = center.getWorld().getName();
	    double squared = range * range;
	    for (Player p : Bukkit.getOnlinePlayers())
	      if ((p.getWorld().getName().equals(name)) && (p.getLocation().distanceSquared(center) <= squared))
	        players.add(p);
	    return players;
	  }

	  private static Object instantiatePacket(String name, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	  {
	    if (amount < 1)
	      throw new PacketInstantiationException("Amount cannot be lower than 1");
	    try {
	      return packetPlayOutWorldParticles.newInstance(name, (float) center.getX(), (float) center.getY(), (float) center.getZ(), offsetX, offsetY, offsetZ, speed, amount);
	    } catch (Exception e) {
	      throw new PacketInstantiationException("Packet instantiation failed", e);
	    }
	  }

	  private static Object instantiateIconCrackPacket(int id, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	  {
	    return instantiatePacket("iconcrack_" + id, center, offsetX, offsetY, offsetZ, speed, amount);
	  }

	  private static Object instantiateBlockCrackPacket(int id, byte data, Location center, float offsetX, float offsetY, float offsetZ, int amount)
	  {
	    return instantiatePacket("blockcrack_" + id + "_" + data, center, offsetX, offsetY, offsetZ, 0.0F, amount);
	  }

	  private static Object instantiateBlockDustPacket(int id, byte data, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	  {
	    return instantiatePacket("blockdust_" + id + "_" + data, center, offsetX, offsetY, offsetZ, speed, amount);
	  }

	  private static void sendPacket(Player p, Object packet)
	  {
	    try
	    {
	      sendPacket.invoke(playerConnection.get(getHandle.invoke(p)), packet);
	    } catch (Exception e) {
	      throw new PacketSendingException("Failed to send a packet to player '" + p.getName() + "'", e);
	    }
	  }

	  private static void sendPacket(Collection<Player> players, Object packet)
	  {
	    for (Player p : players)
	      sendPacket(p, packet);
	  }
	  public void display(Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player players)
	  {
	    sendPacket(Collections.singletonList(
				players), instantiatePacket(this.name, center, offsetX, offsetY, offsetZ, speed, amount));
	  }

	  public void display(Location center, double range, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	  {
	    if (range > MAX_RANGE)
	      throw new IllegalArgumentException("Range cannot exceed the maximum value of 16");
	    sendPacket(getPlayers(center, range), instantiatePacket(this.name, center, offsetX, offsetY, offsetZ, speed, amount));
	  }

	  public void display(Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	  {
	    display(center, MAX_RANGE, offsetX, offsetY, offsetZ, speed, amount);
	  }

	  public static void displayIconCrack(Location center, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player[] players)
	  {
	    sendPacket(Arrays.asList(players), instantiateIconCrackPacket(id, center, offsetX, offsetY, offsetZ, speed, amount));
	  }

	  public static void displayIconCrack(Location center, double range, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	  {
	    if (range > MAX_RANGE)
	      throw new IllegalArgumentException("Range has to be lower/equal the maximum of 16");
	    sendPacket(getPlayers(center, range), instantiateIconCrackPacket(id, center, offsetX, offsetY, offsetZ, speed, amount));
	  }

	  public static void displayIconCrack(Location center, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	  {
	    displayIconCrack(center, MAX_RANGE, id, offsetX, offsetY, offsetZ, speed, amount);
	  }

	  public static void displayBlockCrack(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, int amount, Player[] players)
	  {
	    sendPacket(Arrays.asList(players), instantiateBlockCrackPacket(id, data, center, offsetX, offsetY, offsetZ, amount));
	  }

	  public static void displayBlockCrack(Location center, double range, int id, byte data, float offsetX, float offsetY, float offsetZ, int amount)
	  {
	    if (range > MAX_RANGE)
	      throw new IllegalArgumentException("Range has to be lower/equal the maximum of 16");
	    sendPacket(getPlayers(center, range), instantiateBlockCrackPacket(id, data, center, offsetX, offsetY, offsetZ, amount));
	  }

	  public static void displayBlockCrack(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, int amount)
	  {
	    displayBlockCrack(center, MAX_RANGE, id, data, offsetX, offsetY, offsetZ, amount);
	  }

	  public static void displayBlockDust(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player[] players)
	  {
	    sendPacket(Arrays.asList(players), instantiateBlockDustPacket(id, data, center, offsetX, offsetY, offsetZ, speed, amount));
	  }

	  public static void displayBlockDust(Location center, double range, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	  {
	    if (range > MAX_RANGE)
	      throw new IllegalArgumentException("Range has to be lower/equal the maximum of 16");
	    sendPacket(getPlayers(center, range), instantiateBlockDustPacket(id, data, center, offsetX, offsetY, offsetZ, speed, amount));
	  }

	  public static void displayBlockDust(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	  {
	    displayBlockDust(center, MAX_RANGE, id, data, offsetX, offsetY, offsetZ, speed, amount);
	  }
	 public static void coneEffect(Location loc) {
		    new BukkitRunnable()
		    {
		      double phi = 0.0D;

		      @Override
			public void run() { this.phi += 0.3926990816987241D;

		        for (double t = 0.0D; t <= 6.283185307179586D; t += 0.1963495408493621D) {
		          for (double i = 0.0D; i <= 1.0D; i += 1.0D) {
		            double x = 0.4D * (6.283185307179586D - t) * 0.5D * cos(t + this.phi + i * 3.141592653589793D);
		            double y = 0.5D * t;
		            double z = 0.4D * (6.283185307179586D - t) * 0.5D * sin(t + this.phi + i * 3.141592653589793D);
		            loc.add(x, y, z);
		            ParticleEffect.HEART.display(loc, 0.0F, 0.0F, 0.0F, 0.0F, 1);
		            loc.subtract(x, y, z);
		          }
		        }

		        if (this.phi > 31.415926535897931D)
		          cancel();
		      }
		    }
		    .runTaskTimer(getPlugin(), 0L, 3L);
		  }

		  public static void agualaEffect(Location loc) {
		    new BukkitRunnable() {
		      double phi = 0.0D;

		      @Override
			public void run() { this.phi += 0.3141592653589793D;
		        for (double t = 0.0D; t <= 15.707963267948966D; t += 0.07853981633974483D) {
		          double r = 1.2D;
		          double x = r * cos(t) * sin(this.phi);
		          double y = r * cos(this.phi) + 1.2D;
		          double z = r * sin(t) * sin(this.phi);
		          loc.add(x, y, z);
		          ParticleEffect.DRIP_WATER.display(loc, 0.0F, 0.0F, 0.0F, 0.0F, 1);

		          loc.subtract(x, y, z);
		        }

		        if (this.phi > 3.141592653589793D)
		          cancel();
		      }
		    }
		    .runTaskTimer(getPlugin(), 0L, 1L);
		  }

		  public static void fireBenderEffect(Location loc) {
		    new BukkitRunnable() {
		      double phi = 0.0D;

		      @Override
			public void run() { this.phi += 0.3141592653589793D;
		        for (double t = 0.0D; t <= 15.707963267948966D; t += 0.07853981633974483D) {
		          double r = 1.2D;
		          double x = r * cos(t) * sin(this.phi);
		          double y = r * cos(this.phi) + 1.2D;
		          double z = r * sin(t) * sin(this.phi);
		          loc.add(x, y, z);
		          ParticleEffect.FLAME.display(loc, 0.0F, 0.0F, 0.0F, 0.0F, 1);

		          loc.subtract(x, y, z);
		        }

		        if (this.phi > 3.141592653589793D)
		          cancel();
		      }
		    }
		    .runTaskTimer(getPlugin(), 0L, 1L);
		  }
		  
		  public static final class Reflections
		  {
		    public static Class<?> getClass(String name, PackageType type)
		      throws Exception
		    {
		      return Class.forName(type + "." + name);
		    }

		    public static Class<?> getClass(String name, SubPackageType type) throws Exception {
		      return Class.forName(type + "." + name);
		    }

		    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>[] parameterTypes)
		    {
		      Class<?>[] p = DataType.convertToPrimitive(parameterTypes);
		      for (Constructor<?> c : clazz.getConstructors())
		        if (DataType.equalsArray(DataType.convertToPrimitive(c.getParameterTypes()), p))
		          return c;
		      return null;
		    }

		    public static Constructor<?> getConstructor(String className, PackageType type, Class<?>[] parameterTypes) throws Exception {
		      return getConstructor(getClass(className, type), parameterTypes);
		    }

		    public static Constructor<?> getConstructor(String className, SubPackageType type, Class<?>[] parameterTypes) throws Exception {
		      return getConstructor(getClass(className, type), parameterTypes);
		    }

		    public static Object newInstance(Class<?> clazz, Object[] args) throws Exception {
		      return Objects.requireNonNull(getConstructor(clazz, DataType.convertToPrimitive(args))).newInstance(args);
		    }

		    public static Object newInstance(String className, PackageType type, Object[] args) throws Exception {
		      return newInstance(getClass(className, type), args);
		    }

		    public static Object newInstance(String className, SubPackageType type, Object[] args) throws Exception {
		      return newInstance(getClass(className, type), args);
		    }

		    public static Method getMethod(Class<?> clazz, String name, Class<?>[] parameterTypes)
		    {
		      Class<?>[] p = DataType.convertToPrimitive(parameterTypes);
		      for (Method m : clazz.getMethods())
		        if ((m.getName().equals(name)) && (DataType.equalsArray(DataType.convertToPrimitive(m.getParameterTypes()), p)))
		          return m;
		      return null;
		    }

		    public static Method getMethod(String className, PackageType type, String name, Class<?>[] parameterTypes) throws Exception {
		      return getMethod(getClass(className, type), name, parameterTypes);
		    }

		    public static Method getMethod(String className, SubPackageType type, String name, Class<?>[] parameterTypes) throws Exception {
		      return getMethod(getClass(className, type), name, parameterTypes);
		    }

		    public static Object invokeMethod(String name, Object instance, Object[] args) throws Exception {
		      return Objects.requireNonNull(getMethod(instance.getClass(), name, DataType.convertToPrimitive(args))).invoke(instance, args);
		    }

		    public static Object invokeMethod(Class<?> clazz, String name, Object instance, Object[] args) throws Exception {
		      return Objects.requireNonNull(getMethod(clazz, name, DataType.convertToPrimitive(args))).invoke(instance, args);
		    }

		    public static Object invokeMethod(String className, PackageType type, String name, Object instance, Object[] args) throws Exception {
		      return invokeMethod(getClass(className, type), name, instance, args);
		    }

		    public static Object invokeMethod(String className, SubPackageType type, String name, Object instance, Object[] args) throws Exception {
		      return invokeMethod(getClass(className, type), name, instance, args);
		    }

		    public static Field getField(Class<?> clazz, String name) throws Exception {
		      Field f = clazz.getField(name);
		      f.setAccessible(true);
		      return f;
		    }

		    public static Field getField(String className, PackageType type, String name) throws Exception {
		      return getField(getClass(className, type), name);
		    }

		    public static Field getField(String className, SubPackageType type, String name) throws Exception {
		      return getField(getClass(className, type), name);
		    }

		    public static Field getDeclaredField(Class<?> clazz, String name) throws Exception {
		      Field f = clazz.getDeclaredField(name);
		      f.setAccessible(true);
		      return f;
		    }

		    public static Field getDeclaredField(String className, PackageType type, String name) throws Exception {
		      return getDeclaredField(getClass(className, type), name);
		    }

		    public static Field getDeclaredField(String className, SubPackageType type, String name) throws Exception {
		      return getDeclaredField(getClass(className, type), name);
		    }

		    public static Object getValue(Object instance, String fieldName) throws Exception {
		      return getField(instance.getClass(), fieldName).get(instance);
		    }

		    public static Object getValue(Class<?> clazz, Object instance, String fieldName) throws Exception {
		      return getField(clazz, fieldName).get(instance);
		    }

		    public static Object getValue(String className, PackageType type, Object instance, String fieldName) throws Exception {
		      return getValue(getClass(className, type), instance, fieldName);
		    }

		    public static Object getValue(String className, SubPackageType type, Object instance, String fieldName) throws Exception {
		      return getValue(getClass(className, type), instance, fieldName);
		    }

		    public static Object getDeclaredValue(Object instance, String fieldName) throws Exception {
		      return getDeclaredField(instance.getClass(), fieldName).get(instance);
		    }

		    public static Object getDeclaredValue(Class<?> clazz, Object instance, String fieldName) throws Exception {
		      return getDeclaredField(clazz, fieldName).get(instance);
		    }

		    public static Object getDeclaredValue(String className, PackageType type, Object instance, String fieldName) throws Exception {
		      return getDeclaredValue(getClass(className, type), instance, fieldName);
		    }

		    public static Object getDeclaredValue(String className, SubPackageType type, Object instance, String fieldName) throws Exception {
		      return getDeclaredValue(getClass(className, type), instance, fieldName);
		    }

		    public static void setValue(Object instance, String fieldName, Object fieldValue) throws Exception {
		      Field f = getField(instance.getClass(), fieldName);
		      f.set(instance, fieldValue);
		    }

		    public static void setValue(Object instance, FieldPair pair) throws Exception {
		      setValue(instance, pair.getName(), pair.getValue());
		    }

		    public static void setValue(Class<?> clazz, Object instance, String fieldName, Object fieldValue) throws Exception {
		      Field f = getField(clazz, fieldName);
		      f.set(instance, fieldValue);
		    }

		    public static void setValue(Class<?> clazz, Object instance, FieldPair pair) throws Exception {
		      setValue(clazz, instance, pair.getName(), pair.getValue());
		    }

		    public static void setValue(String className, PackageType type, Object instance, String fieldName, Object fieldValue) throws Exception {
		      setValue(getClass(className, type), instance, fieldName, fieldValue);
		    }

		    public static void setValue(String className, PackageType type, Object instance, FieldPair pair) throws Exception {
		      setValue(className, type, instance, pair.getName(), pair.getValue());
		    }

		    public static void setValue(String className, SubPackageType type, Object instance, String fieldName, Object fieldValue) throws Exception {
		      setValue(getClass(className, type), instance, fieldName, fieldValue);
		    }

		    public static void setValue(String className, SubPackageType type, Object instance, FieldPair pair) throws Exception {
		      setValue(className, type, instance, pair.getName(), pair.getValue());
		    }

		    public static void setValues(Object instance, FieldPair[] pairs) throws Exception {
		      for (FieldPair pair : pairs)
		        setValue(instance, pair);
		    }

		    public static void setValues(Class<?> clazz, Object instance, FieldPair[] pairs) throws Exception {
		      for (FieldPair pair : pairs)
		        setValue(clazz, instance, pair);
		    }

		    public static void setValues(String className, PackageType type, Object instance, FieldPair[] pairs) throws Exception {
		      setValues(getClass(className, type), instance, pairs);
		    }

		    public static void setValues(String className, SubPackageType type, Object instance, FieldPair[] pairs) throws Exception {
		      setValues(getClass(className, type), instance, pairs);
		    }

		    public static void setDeclaredValue(Object instance, String fieldName, Object fieldValue) throws Exception {
		      Field f = getDeclaredField(instance.getClass(), fieldName);
		      f.set(instance, fieldValue);
		    }

		    public static void setDeclaredValue(Object instance, FieldPair pair) throws Exception {
		      setDeclaredValue(instance, pair.getName(), pair.getValue());
		    }

		    public static void setDeclaredValue(Class<?> clazz, Object instance, String fieldName, Object fieldValue) throws Exception {
		      Field f = getDeclaredField(clazz, fieldName);
		      f.set(instance, fieldValue);
		    }

		    public static void setDeclaredValue(Class<?> clazz, Object instance, FieldPair pair) throws Exception {
		      setDeclaredValue(clazz, instance, pair.getName(), pair.getValue());
		    }

		    public static void setDeclaredValue(String className, PackageType type, Object instance, String fieldName, Object fieldValue) throws Exception {
		      setDeclaredValue(getClass(className, type), instance, fieldName, fieldValue);
		    }

		    public static void setDeclaredValue(String className, PackageType type, Object instance, FieldPair pair) throws Exception {
		      setDeclaredValue(className, type, instance, pair.getName(), pair.getValue());
		    }

		    public static void setDeclaredValue(String className, SubPackageType type, Object instance, String fieldName, Object fieldValue) throws Exception {
		      setDeclaredValue(getClass(className, type), instance, fieldName, fieldValue);
		    }

		    public static void setDeclaredValue(String className, SubPackageType type, Object instance, FieldPair pair) throws Exception {
		      setDeclaredValue(className, type, instance, pair.getName(), pair.getValue());
		    }

		    public static void setDeclaredValues(Object instance, FieldPair[] pairs) throws Exception {
		      for (FieldPair pair : pairs)
		        setDeclaredValue(instance, pair);
		    }

		    public static void setDeclaredValues(Class<?> clazz, Object instance, FieldPair[] pairs) throws Exception {
		      for (FieldPair pair : pairs)
		        setDeclaredValue(clazz, instance, pair);
		    }

		    public static void setDeclaredValues(String className, PackageType type, Object instance, FieldPair[] pairs) throws Exception {
		      setDeclaredValues(getClass(className, type), instance, pairs);
		    }

		    public static void setDeclaredValues(String className, SubPackageType type, Object instance, FieldPair[] pairs) throws Exception {
		      setDeclaredValues(getClass(className, type), instance, pairs);
		    }

		    public enum DataType
		    {
		      BYTE(Byte.TYPE, Byte.class), 
		      SHORT(Short.TYPE, Short.class), 
		      INTEGER(Integer.TYPE, Integer.class), 
		      LONG(Long.TYPE, Long.class), 
		      CHARACTER(Character.TYPE, Character.class), 
		      FLOAT(Float.TYPE, Float.class), 
		      DOUBLE(Double.TYPE, Double.class), 
		      BOOLEAN(Boolean.TYPE, Boolean.class);

		      private static final Map<Class<?>, DataType> CLASS_MAP;
		      private final Class<?> primitive;
		      private final Class<?> reference;

		      static { CLASS_MAP = new HashMap<>();

		        for (DataType t : values()) {
		          CLASS_MAP.put(t.primitive, t);
		          CLASS_MAP.put(t.reference, t);
		        } }

		      DataType(Class<?> primitive, Class<?> reference)
		      {
		        this.primitive = primitive;
		        this.reference = reference;
		      }

		      public Class<?> getPrimitive() {
		        return this.primitive;
		      }

		      public Class<?> getReference() {
		        return this.reference;
		      }

		      public static DataType fromClass(Class<?> c) {
		        return CLASS_MAP.get(c);
		      }

		      public static Class<?> getPrimitive(Class<?> c) {
		        DataType t = fromClass(c);
		        return t == null ? c : t.getPrimitive();
		      }

		      public static Class<?> getReference(Class<?> c) {
		        DataType t = fromClass(c);
		        return t == null ? c : t.getReference();
		      }

		      public static Class<?>[] convertToPrimitive(Class<?>[] classes)
		      {
		        int length = classes == null ? 0 : classes.length;
		        Class<?>[] types = new Class[length];
		        for (int i = 0; i < length; i++)
		          types[i] = getPrimitive(classes[i]);
		        return types;
		      }

		      public static Class<?>[] convertToPrimitive(Object[] objects)
		      {
		        int length = objects == null ? 0 : objects.length;
		        Class<?>[] types = new Class[length];
		        for (int i = 0; i < length; i++)
		          types[i] = getPrimitive(objects[i].getClass());
		        return types;
		      }

		      public static boolean equalsArray(Class<?>[] a1, Class<?>[] a2) {
		        if ((a1 == null) || (a2 == null) || (a1.length != a2.length))
		          return false;
		        for (int i = 0; i < a1.length; i++)
		          if ((!a1[i].equals(a2[i])) && (!a1[i].isAssignableFrom(a2[i])))
		            return false;
		        return true;
		      }
		    }

		    public static final class FieldPair
		    {
		      private final String name;
		      private final Object value;

		      public FieldPair(String name, Object value) {
		        this.name = name;
		        this.value = value;
		      }

		      public String getName() {
		        return this.name;
		      }

		      public Object getValue() {
		        return this.value;
		      }
		    }

		    public enum PackageType
		    {
		      MINECRAFT_SERVER("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().substring(23)), 
		      CRAFTBUKKIT(Bukkit.getServer().getClass().getPackage().getName());

		      private final String name;

		      PackageType(String name) {
		        this.name = name;
		      }

		      public String getName() {
		        return this.name;
		      }

		      @Override
		  	public String toString()
		      {
		        return this.name;
		      }
		    }

		    public enum PacketType
		    {
		      HANDSHAKING_IN_SET_PROTOCOL("PacketHandshakingInSetProtocol"), 
		      LOGIN_IN_ENCRYPTION_BEGIN("PacketLoginInEncryptionBegin"), 
		      LOGIN_IN_START("PacketLoginInStart"), 
		      LOGIN_OUT_DISCONNECT("PacketLoginOutDisconnect"), 
		      LOGIN_OUT_ENCRYPTION_BEGIN("PacketLoginOutEncryptionBegin"), 
		      LOGIN_OUT_SUCCESS("PacketLoginOutSuccess"), 
		      PLAY_IN_ABILITIES("PacketPlayInAbilities"), 
		      PLAY_IN_ARM_ANIMATION("PacketPlayInArmAnimation"), 
		      PLAY_IN_BLOCK_DIG("PacketPlayInBlockDig"), 
		      PLAY_IN_BLOCK_PLACE("PacketPlayInBlockPlace"), 
		      PLAY_IN_CHAT("PacketPlayInChat"), 
		      PLAY_IN_CLIENT_COMMAND("PacketPlayInClientCommand"), 
		      PLAY_IN_CLOSE_WINDOW("PacketPlayInCloseWindow"), 
		      PLAY_IN_CUSTOM_PAYLOAD("PacketPlayInCustomPayload"), 
		      PLAY_IN_ENCHANT_ITEM("PacketPlayInEnchantItem"), 
		      PLAY_IN_ENTITY_ACTION("PacketPlayInEntityAction"), 
		      PLAY_IN_FLYING("PacketPlayInFlying"), 
		      PLAY_IN_HELD_ITEM_SLOT("PacketPlayInHeldItemSlot"), 
		      PLAY_IN_KEEP_ALIVE("PacketPlayInKeepAlive"), 
		      PLAY_IN_LOOK("PacketPlayInLook"), 
		      PLAY_IN_POSITION("PacketPlayInPosition"), 
		      PLAY_IN_POSITION_LOOK("PacketPlayInPositionLook"), 
		      PLAY_IN_SET_CREATIVE_SLOT("PacketPlayInSetCreativeSlot"), 
		      PLAY_IN_SETTINGS("PacketPlayInSettings"), 
		      PLAY_IN_STEER_VEHICLE("PacketPlayInSteerVehicle"), 
		      PLAY_IN_TAB_COMPLETE("PacketPlayInTabComplete"), 
		      PLAY_IN_TRANSACTION("PacketPlayInTransaction"), 
		      PLAY_IN_UPDATE_SIGN("PacketPlayInUpdateSign"), 
		      PLAY_IN_USE_ENTITY("PacketPlayInUseEntity"), 
		      PLAY_IN_WINDOW_CLICK("PacketPlayInWindowClick"), 
		      PLAY_OUT_ABILITIES("PacketPlayOutAbilities"), 
		      PLAY_OUT_ANIMATION("PacketPlayOutAnimation"), 
		      PLAY_OUT_ATTACH_ENTITY("PacketPlayOutAttachEntity"), 
		      PLAY_OUT_BED("PacketPlayOutBed"), 
		      PLAY_OUT_BLOCK_ACTION("PacketPlayOutBlockAction"), 
		      PLAY_OUT_BLOCK_BREAK_ANIMATION("PacketPlayOutBlockBreakAnimation"), 
		      PLAY_OUT_BLOCK_CHANGE("PacketPlayOutBlockChange"), 
		      PLAY_OUT_CHAT("PacketPlayOutChat"), 
		      PLAY_OUT_CLOSE_WINDOW("PacketPlayOutCloseWindow"), 
		      PLAY_OUT_COLLECT("PacketPlayOutCollect"), 
		      PLAY_OUT_CRAFT_PROGRESS_BAR("PacketPlayOutCraftProgressBar"), 
		      PLAY_OUT_CUSTOM_PAYLOAD("PacketPlayOutCustomPayload"), 
		      PLAY_OUT_ENTITY("PacketPlayOutEntity"), 
		      PLAY_OUT_ENTITY_DESTROY("PacketPlayOutEntityDestroy"), 
		      PLAY_OUT_ENTITY_EFFECT("PacketPlayOutEntityEffect"), 
		      PLAY_OUT_ENTITY_EQUIPMENT("PacketPlayOutEntityEquipment"), 
		      PLAY_OUT_ENTITY_HEAD_ROTATION("PacketPlayOutEntityHeadRotation"), 
		      PLAY_OUT_ENTITY_LOOK("PacketPlayOutEntityLook"), 
		      PLAY_OUT_ENTITY_METADATA("PacketPlayOutEntityMetadata"), 
		      PLAY_OUT_ENTITY_STATUS("PacketPlayOutEntityStatus"), 
		      PLAY_OUT_ENTITY_TELEPORT("PacketPlayOutEntityTeleport"), 
		      PLAY_OUT_ENTITY_VELOCITY("PacketPlayOutEntityVelocity"), 
		      PLAY_OUT_EXPERIENCE("PacketPlayOutExperience"), 
		      PLAY_OUT_EXPLOSION("PacketPlayOutExplosion"), 
		      PLAY_OUT_GAME_STATE_CHANGE("PacketPlayOutGameStateChange"), 
		      PLAY_OUT_HELD_ITEM_SLOT("PacketPlayOutHeldItemSlot"), 
		      PLAY_OUT_KEEP_ALIVE("PacketPlayOutKeepAlive"), 
		      PLAY_OUT_KICK_DISCONNECT("PacketPlayOutKickDisconnect"), 
		      PLAY_OUT_LOGIN("PacketPlayOutLogin"), 
		      PLAY_OUT_MAP("PacketPlayOutMap"), 
		      PLAY_OUT_MAP_CHUNK("PacketPlayOutMapChunk"), 
		      PLAY_OUT_MAP_CHUNK_BULK("PacketPlayOutMapChunkBulk"), 
		      PLAY_OUT_MULTI_BLOCK_CHANGE("PacketPlayOutMultiBlockChange"), 
		      PLAY_OUT_NAMED_ENTITY_SPAWN("PacketPlayOutNamedEntitySpawn"), 
		      PLAY_OUT_NAMED_SOUND_EFFECT("PacketPlayOutNamedSoundEffect"), 
		      PLAY_OUT_OPEN_SIGN_EDITOR("PacketPlayOutOpenSignEditor"), 
		      PLAY_OUT_OPEN_WINDOW("PacketPlayOutOpenWindow"), 
		      PLAY_OUT_PLAYER_INFO("PacketPlayOutPlayerInfo"), 
		      PLAY_OUT_POSITION("PacketPlayOutPosition"), 
		      PLAY_OUT_REL_ENTITY_MOVE("PacketPlayOutRelEntityMove"), 
		      PLAY_OUT_REL_ENTITY_MOVE_LOOK("PacketPlayOutRelEntityMoveLook"), 
		      PLAY_OUT_REMOVE_ENTITY_EFFECT("PacketPlayOutRemoveEntityEffect"), 
		      PLAY_OUT_RESPAWN("PacketPlayOutRespawn"), 
		      PLAY_OUT_SCOREBOARD_DISPLAY_OBJECTIVE("PacketPlayOutScoreboardDisplayObjective"), 
		      PLAY_OUT_SCOREBOARD_OBJECTIVE("PacketPlayOutScoreboardObjective"), 
		      PLAY_OUT_SCOREBOARD_SCORE("PacketPlayOutScoreboardScore"), 
		      PLAY_OUT_SCOREBOARD_TEAM("PacketPlayOutScoreboardTeam"), 
		      PLAY_OUT_SET_SLOT("PacketPlayOutSetSlot"), 
		      PLAY_OUT_SPAWN_ENTITY("PacketPlayOutSpawnEntity"), 
		      PLAY_OUT_SPAWN_ENTITY_EXPERIENCE_ORB("PacketPlayOutSpawnEntityExperienceOrb"), 
		      PLAY_OUT_SPAWN_ENTITY_LIVING("PacketPlayOutSpawnEntityLiving"), 
		      PLAY_OUT_SPAWN_ENTITY_PAINTING("PacketPlayOutSpawnEntityPainting"), 
		      PLAY_OUT_SPAWN_ENTITY_WEATHER("PacketPlayOutSpawnEntityWeather"), 
		      PLAY_OUT_SPAWN_POSITION("PacketPlayOutSpawnPosition"), 
		      PLAY_OUT_STATISTIC("PacketPlayOutStatistic"), 
		      PLAY_OUT_TAB_COMPLETE("PacketPlayOutTabComplete"), 
		      PLAY_OUT_TILE_ENTITY_DATA("PacketPlayOutTileEntityData"), 
		      PLAY_OUT_TRANSACTION("PacketPlayOutTransaction"), 
		      PLAY_OUT_UPDATE_ATTRIBUTES("PacketPlayOutUpdateAttributes"), 
		      PLAY_OUT_UPDATE_HEALTH("PacketPlayOutUpdateHealth"), 
		      PLAY_OUT_UPDATE_SIGN("PacketPlayOutUpdateSign"), 
		      PLAY_OUT_UPDATE_TIME("PacketPlayOutUpdateTime"), 
		      PLAY_OUT_WINDOW_ITEMS("PacketPlayOutWindowItems"), 
		      PLAY_OUT_WORLD_EVENT("PacketPlayOutWorldEvent"), 
		      PLAY_OUT_WORLD_PARTICLES("PacketPlayOutWorldParticles"), 
		      STATUS_IN_PING("PacketStatusInPing"), 
		      STATUS_IN_START("PacketStatusInStart"), 
		      STATUS_OUT_PONG("PacketStatusOutPong"), 
		      STATUS_OUT_SERVER_INFO("PacketStatusOutServerInfo");

		      private final String name;
		      private Class<?> packet;

		      PacketType(String name) { this.name = name; }

		      public String getName()
		      {
		        return name;
		      }

		      public Class<?> getPacket() throws Exception {
		        return this.packet == null ? (this.packet = Reflections.getClass(this.name, Reflections.PackageType.MINECRAFT_SERVER)) : this.packet;
		      }
		    }

		    public enum SubPackageType
		    {
		      BLOCK, 
		      CHUNKIO, 
		      COMMAND, 
		      CONVERSATIONS, 
		      ENCHANTMENS, 
		      ENTITY, 
		      EVENT, 
		      GENERATOR, 
		      HELP, 
		      INVENTORY, 
		      MAP, 
		      METADATA, 
		      POTION, 
		      PROJECTILES, 
		      SCHEDULER, 
		      SCOREBOARD, 
		      UPDATER, 
		      UTIL;

		      private final String name;

		      SubPackageType() {
		        this.name = (Reflections.PackageType.CRAFTBUKKIT + "." + name().toLowerCase());
		      }

		      public String getName() {
		        return this.name;
		      }

		      @Override
		  	public String toString()
		      {
		        return this.name;
		      }
		    }
		  }
}
