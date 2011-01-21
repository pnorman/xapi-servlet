package com.yellowbkpk.geo.xapi.db;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.postgis.PGgeometry;

public abstract class Selector {

    protected String where = "";
    protected List<Object> param = new LinkedList<Object>();

    public Selector(String string, Object... params) {
    	this.where = string;
    	for (Object object : params) {
			this.param.add(object);
		}
	}

	private static String randomKey() {
		return UUID.randomUUID().toString().substring(0, 4);
	}

	public String getWhereString() {
        return this.where;
    }
    public List<Object> getWhereParam() {
        return this.param;
    }

    public static class Tag extends Selector {
        public Tag(String key, String value) {
        	super(" tags @> hstore(?, ?)", key, value);
        }
        
        public static class Wildcard extends Selector {
            public Wildcard(String key) {
            	super(" exist(tags, ?)", key);
            }
        }
    }

    public static class BoundingBox extends Selector {
        private double left;
		private double right;
		private double bottom;
		private double top;

		public BoundingBox(double left, double right, double top, double bottom) {
        	super(" geom && ?", new PGgeometry(PolygonBuilder.buildBoundingPolygon(left, right, top, bottom)));
        	this.left = left;
        	this.right = right;
        	this.bottom = bottom;
        	this.top = top;
        }

		public double getLeft() {
			return left;
		}

		public double getRight() {
			return right;
		}

		public double getTop() {
			return top;
		}

		public double getBottom() {
			return bottom;
		}
    }

    public class Changeset extends Selector {
        public Changeset(int changeset) {
            super(" changeset_id == ?", changeset);
        }
    }

    public class Uid extends Selector {
        public Uid(int uid) {
            super(" user_id == ?", uid);
        }
    }
}