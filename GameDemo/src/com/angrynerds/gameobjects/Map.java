package com.angrynerds.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * User: Franjo
 * Date: 26.10.13
 * Time: 20:30
 * Project: Main
 */
public class Map extends GameObject {

    // debug controlls
    private static final boolean SHOW_GRID = false;
    private static final boolean SHOW_COLLISION_SHAPES = true;
    private Texture gridTexture;
    private Texture collisionShapeTexture;

    // constants
    private static final int HORIZONTAL_FLIP = 0;
    private static final int VERTICAL_FLIP = 1;
    private static final int BOTH_FLIP = 2;

    private BitmapFont font;

    //    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    // map properties
    private int numTilesX;
    private int numTilesY;

    private int tileWidth;
    private int tileHeight;
    private int mapWidth;
    private int mapHeight;

    // player relevant subjects
    private Vector2 spawn;

    private Array<TiledMapTileLayer> collsionLayers;
    private Array<Rectangle> collisionObjects;

    private String mapPath = "data/maps/map_000.tmx";

    // global helper variables
    private Array<Rectangle> qArray = new Array<Rectangle>();

    public Map(OrthographicCamera camera) {
        this.camera = camera;

        init();
    }

    public Map() {

    }

    private void init() {
        map = new TmxMapLoader().load(mapPath);
        renderer = new OrthogonalTiledMapRenderer(map);
        renderer.setView(camera);

        // parse map properties
        int qWidth = Integer.parseInt(map.getProperties().get("width").toString());
        int qHeight = Integer.parseInt(map.getProperties().get("height").toString());
        int qTileWidth = Integer.parseInt(map.getProperties().get("tilewidth").toString());
        int qTileHeight = Integer.parseInt(map.getProperties().get("tileheight").toString());

        numTilesX = qWidth;
        numTilesY = qHeight;
        tileWidth = qTileWidth;
        tileHeight = qTileHeight;
        mapWidth = numTilesX * tileWidth;
        mapHeight = numTilesY * tileHeight;

        if (SHOW_GRID) {
            Pixmap pixmap = new Pixmap(mapWidth, mapHeight, Pixmap.Format.RGBA8888);
            pixmap.setColor(0, 0, 0, 1);
            for (int h = 0; h < mapHeight; h++) {
                for (int w = 0; w < mapWidth; w++) {
                    pixmap.drawRectangle(w * tileWidth, h * tileHeight, tileWidth, tileHeight);
                }
            }

            gridTexture = new Texture(pixmap);
//            font = new BitmapFont(Gdx.files.internal("com/badlogic/gdx/utils/arial-15.fnt"), Gdx.files.internal("com/badlogic/gdx/utils/arial-15.png"), false);
//            font.setColor(0, 0, 0, 1);
//            font.scale(0.2f);
        }

        // fill collision relevant lists
        MapLayers layers = map.getLayers();

        collsionLayers = new Array<TiledMapTileLayer>();
        collisionObjects = new Array<Rectangle>();
        for (int i = 0; i < layers.getCount(); i++) {
            if (layers.get(i).getName().startsWith("$c.")) {
                // tile layer
                if (layers.get(i).getObjects().getCount() == 0) {
//                    collsionLayers.add(flipTiledMapTileLayer((TiledMapTileLayer) layers.get(i), HORIZONTAL_FLIP));
                    collsionLayers.add((TiledMapTileLayer) layers.get(i));
//                    TiledMapTileLayer g = (TiledMapTileLayer) layers.get(i);

                    // object layer
                } else {
                    Array<HashMap<String, String>> objects = getObjectGroups(layers.get(i));
//                    flipObjectLayerRectangles(objects,1);
//                    MapLayer mapLayer = (flipObjectLayerRectangles(layers.get(i), 1));
//                    MapLayer mapLayer = layers.get(i);

//                    MapObjects objects = mapLayer.getObjects();
//                    System.out.println("obhbcjks:" + objects.size);

                    for (int j = 0; j < objects.size; j++) {
                        System.out.println("" + j);
                        if (objects.get(j).containsKey("width") &&
                                objects.get(j).containsKey("height") &&
                                objects.get(j).containsKey("x") &&
                                objects.get(j).containsKey("y")) {

                            float qX = Float.parseFloat(objects.get(j).get("x").toString());
                            float qY = Float.parseFloat(objects.get(j).get("y").toString());
                            float qW = Float.parseFloat(objects.get(j).get("width").toString());
                            float qH = Float.parseFloat(objects.get(j).get("height").toString());

                            System.out.println(qX + " " + qY + " " + qW + " " + qH);

                            collisionObjects.add(new Rectangle(qX, mapHeight - qY - qH, qW, qH));

                        }
                    }
                }
            }

            // subjectlayer
            else if (layers.get(i).getName().startsWith("$s.")) {
                // tiledMapTileLayerd
                if (layers.get(i).getObjects().getCount() == 0) {
                    System.out.println("kmvfds<dcwasfvaesdfcsdc");
                    TiledMapTileLayer l = (TiledMapTileLayer) layers.get(i);

                    for (int j = 0; j < l.getHeight(); j++) {
                        for (int k = 0; k < l.getWidth(); k++) {
                            if (l.getCell(k, j) != null) {
                                if (l.getCell(k, j).getTile().getProperties().containsKey("spawn")) {
                                    System.out.println("SPAWN FOUND");
                                    float qX = k * tileWidth;
                                    float qY = j * tileHeight;
                                    spawn = new Vector2(qX, qY);
                                }
                            }
                        }
                    }
                }
            }


        }

        /* draw the collision shapes */
        if (SHOW_COLLISION_SHAPES) {
            Pixmap p = new Pixmap(mapWidth, mapHeight, Pixmap.Format.RGBA8888);
            Color color_outline = new Color(0, 0, 0, 1);
            Color color_fill = new Color(1, 0, 0, 0.3f);
            /* draws the collision rectangles */
            for (int i = 0; i < collisionObjects.size; i++) {
                Rectangle r = collisionObjects.get(i);
                p.setColor(color_outline);
//                p.drawRectangle((int) (r.getX()), (int) (mapHeight - r.getHeight() - (r.getY())), (int) (r.getWidth()), (int) (r.getHeight()));
                p.drawRectangle((int) (r.getX()), (int) (r.getY()), (int) (r.getWidth()), (int) (r.getHeight()));
                p.setColor(color_fill);
                p.fillRectangle((int) (r.getX()), (int) (r.getY()), (int) (r.getWidth()), (int) (r.getHeight()));

            }
            /*draws the collision tiles */
            for (int j = 0; j < collsionLayers.size; j++) {
                for (int h = 0; h < numTilesY; h++) {
                    for (int w = 0; w < numTilesX; w++) {
                        TiledMapTileLayer layer = flipTiledMapTileLayer(collsionLayers.get(j), HORIZONTAL_FLIP);
                        TiledMapTileLayer.Cell cell = layer.getCell(w, h);
                        if (cell != null) {
                            p.setColor(color_outline);
                            p.drawRectangle(w * tileWidth, h * tileHeight, tileWidth, tileHeight);
                            p.setColor(color_fill);
                            p.fillRectangle(w * tileWidth, h * tileHeight, tileWidth, tileHeight);
                        }
                    }
                }
            }

            /* create collision shape texture */
            collisionShapeTexture = new Texture(p);
        }

    }

    private Array<HashMap<String, String>> getObjectGroups(MapLayer layer) {

        final String layername = layer.getName();
        System.out.println("layername: " + layername);
        Array<HashMap<String, String>> objects = new Array<HashMap<String, String>>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(mapPath));
            String line = reader.readLine();
            while (line != null) {
                if (line.trim().startsWith("<objectgroup") && line.contains("name=\"" + layername + "\"")) {
                    line = reader.readLine();
                    while (line.trim().startsWith("<object")) {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        objects.add(hm);
                        String[] properties = line.trim().split(" ");
                        for (int i = 1; i < properties.length; i++) {
                            String k = properties[i].split("=")[0];
                            String v = properties[i].split("=")[1].replace('"', ' ').trim().replace("/>", "");

                            hm.put(k, v);
                        }

                        line = reader.readLine();
                    }
                }

                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return objects;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.disableBlending();
        renderer.render();
        batch.enableBlending();

        if (SHOW_GRID) {
            batch.begin();
            batch.draw(gridTexture, 0, 0);

//            for (int h = 0; h < mapHeight; h++) {
//                for (int w = 0; w < mapWidth; w++) {
//                    font.draw(batch, "" + (h * mapWidth + w), w * tileWidth, h * tileHeight);
//                }
//            }
            batch.end();
        }

        if (SHOW_COLLISION_SHAPES) {
            batch.begin();
            batch.draw(collisionShapeTexture, 0, 0);
            batch.end();
        }


    }

    @Override
    public void update(float deltaTime) {
        renderer.setView(camera);
    }

    /**
     * checks whether there is a solid tile at specified position
     *
     * @param x position x
     * @param y position y
     * @return whether point collides with solid tile or not
     */
    public boolean isSolid(final float x, final float y) {

        for (int i = 0; i < collsionLayers.size; i++) {
            TiledMapTileLayer.Cell cell = collsionLayers.get(i).getCell((int) (x) / tileWidth, (int) (y) / tileHeight);
            if (cell != null) {
                return true;
            }
        }
        return false;
    }

    public Array<Rectangle> getCollisionObjects(final float x, final float y) {
        if (qArray.size != 0) qArray.clear();
        for (int i = 0; i < collisionObjects.size; i++) {
            if (collisionObjects.get(i).contains(x, y)) {
                qArray.add(collisionObjects.get(i));
            }
        }
        return qArray;
    }

    private MapLayer flipObjectLayerRectangles(final MapLayer layer, final int flipKind) {
        if (layer.getObjects().getCount() == 0) {
            throw new IllegalArgumentException(layer.getName() + " must be an object layer!");
        } else {
            Array<HashMap<String, String>> objects = getObjectGroups(layer);
//            MapLayer copy = new MapLayer();
            /* clone layer properties */
            MapLayer copy = new MapLayer();
            copy.getProperties().clear();
//            copy.getProperties().putAll(layer.getProperties());
            copy.setName(layer.getName());
            copy.setOpacity(layer.getOpacity());
            copy.setVisible(layer.isVisible());

            System.out.println("object lenght: " + objects.size);

            for (int j = 0; j < objects.size; j++) {
                if (objects.get(j).containsKey("width") &&
                        objects.get(j).containsKey("height") &&
                        objects.get(j).containsKey("x") &&
                        objects.get(j).containsKey("y")) {


                    float qX = Float.parseFloat(objects.get(j).get("x").toString());
                    float qY = Float.parseFloat(objects.get(j).get("y").toString());
                    float qW = Float.parseFloat(objects.get(j).get("width").toString());
                    float qH = Float.parseFloat(objects.get(j).get("height").toString());

                    MapObject o = new MapObject();
                    o.getProperties().put("width", qW);
                    o.getProperties().put("height", qH);
                    o.getProperties().put("x", "" + qX);
                    o.getProperties().put("y", "" + (mapHeight - qY - Float.parseFloat(o.getProperties().get("height").toString())));


                    copy.getObjects().add(o);
                }
            }

            assert (copy.getObjects().getCount() != 0) : "flipped object layer must not have an empty object list";

            return copy;
        }
    }

    private void flipObjectLayerRectangles(Array<HashMap<String, String>> objects, final int flipKind) {
        if (objects.size == 0) {
            throw new IllegalArgumentException("object array size must not be 0");
        } else {
            ///
            for (int j = 0; j < objects.size; j++) {
                if (objects.get(j).containsKey("width") &&
                        objects.get(j).containsKey("height") &&
                        objects.get(j).containsKey("x") &&
                        objects.get(j).containsKey("y")) {

                    System.out.println("dsadasdas");

                    float qX = Float.parseFloat(objects.get(j).get("x").toString());
                    float qY = Float.parseFloat(objects.get(j).get("y").toString());
                    float qW = Float.parseFloat(objects.get(j).get("width").toString());
                    float qH = Float.parseFloat(objects.get(j).get("height").toString());

                    objects.get(j).put("width", "" + qW);
                    objects.get(j).put("height", "" + qH);
                    objects.get(j).put("x", "" + qX);
                    objects.get(j).put("y", "" + (mapHeight - qH - qY));
//
//                    MapObject o = new MapObject();
//                    objects.get(j).getProperties().put("width", qW);
//                    o.getProperties().put("height", qH);
//                    o.getProperties().put("x", "" + qX);
//                    o.getProperties().put("y", "" + (mapHeight - Float.parseFloat(o.getProperties().get("height").toString())));
//
//
//                    copy.getObjects().add(o);


//            Array<HashMap<String, String>> objects = getObjectGroups(layer);
//            MapLayer copy = new MapLayer();
            /* clone layer properties */
//                    MapLayer copy = new MapLayer();
//                    copy.getProperties().clear();
////            copy.getProperties().putAll(layer.getProperties());
//                    copy.setName(layer.getName());
//                    copy.setOpacity(layer.getOpacity());
//                    copy.setVisible(layer.isVisible());
//
//                    System.out.println("object lenght: " + objects.size);
//
//                    for (int j = 0; j < objects.size; j++) {
//                        if (objects.get(j).containsKey("width") &&
//                                objects.get(j).containsKey("height") &&
//                                objects.get(j).containsKey("x") &&
//                                objects.get(j).containsKey("y")) {
//
//
//                            float qX = Float.parseFloat(objects.get(j).get("x").toString());
//                            float qY = Float.parseFloat(objects.get(j).get("y").toString());
//                            float qW = Float.parseFloat(objects.get(j).get("width").toString());
//                            float qH = Float.parseFloat(objects.get(j).get("height").toString());
//
//                            MapObject o = new MapObject();
//                            o.getProperties().put("width", qW);
//                            o.getProperties().put("height", qH);
//                            o.getProperties().put("x", "" + qX);
//                            o.getProperties().put("y", "" + (mapHeight - Float.parseFloat(o.getProperties().get("height").toString())));
//
//
//                            copy.getObjects().add(o);
                }
            }

//                    assert (copy.getObjects().getCount() != 0) : "flipped object layer must not have an empty object list";
//
//                    return copy;
//            return objects;
        }
    }

    /**
     * flips a TiledMapTileLayer depending on the flipKind
     *
     * @param layer    TileMapTileLayer that should be flipped
     * @param flipKind the way the layer should be flipped
     * @return a flipped copy of the layer
     */
    private TiledMapTileLayer flipTiledMapTileLayer(final TiledMapTileLayer layer, final int flipKind) {
        TiledMapTileLayer copy = new TiledMapTileLayer(layer.getWidth(), layer.getHeight(), (int) (layer.getTileWidth()), (int) (layer.getTileHeight()));
        switch (flipKind) {
            case HORIZONTAL_FLIP:
                for (int h = 0; h < layer.getHeight(); h++) {
                    for (int w = 0; w < layer.getWidth(); w++) {
                        copy.setCell(w, layer.getHeight() - h - 1, layer.getCell(w, h));
                    }
                }
                break;
            case VERTICAL_FLIP:
                for (int h = 0; h < layer.getHeight(); h++) {
                    for (int w = 0; w < layer.getWidth(); w++) {
                        copy.setCell(layer.getWidth() - w - 1, h, layer.getCell(w, h));
                    }
                }
                break;
            case BOTH_FLIP:
                for (int h = 0; h < layer.getHeight(); h++) {
                    for (int w = 0; w < layer.getWidth(); w++) {
                        copy.setCell(layer.getWidth() - w - 1, layer.getHeight() - h - 1, layer.getCell(w, h));
                    }
                }
                break;
            default:
                break;
        }


        return copy;
    }

    public Vector2 getSpawn() {
        return spawn;
    }

    //<editor-fold desc="map property getters">
    public int getMapHeight() {
        return mapHeight;
    }

    public int getNumTilesX() {
        return numTilesX;
    }

    public int getNumTilesY() {
        return numTilesY;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }
    //</editor-fold>
}
