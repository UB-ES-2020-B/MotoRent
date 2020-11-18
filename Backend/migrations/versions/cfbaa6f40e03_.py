"""empty message

Revision ID: cfbaa6f40e03
Revises: 
Create Date: 2020-11-15 20:16:16.156086

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = 'cfbaa6f40e03'
down_revision = None
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.create_table('map_coords',
    sa.Column('id', sa.Integer(), autoincrement=True, nullable=False),
    sa.Column('from_latitude', sa.Float(), nullable=False),
    sa.Column('from_longitude', sa.Float(), nullable=False),
    sa.Column('to_latitude', sa.Float(), nullable=False),
    sa.Column('to_longitude', sa.Float(), nullable=False),
    sa.PrimaryKeyConstraint('id'),
    sa.UniqueConstraint('id')
    )
    op.create_table('motos',
    sa.Column('id', sa.Integer(), autoincrement=True, nullable=False),
    sa.Column('license_number', sa.String(), nullable=False),
    sa.Column('battery', sa.Integer(), nullable=False),
    sa.Column('available', sa.Boolean(), nullable=False),
    sa.Column('latitude', sa.Float(), nullable=False),
    sa.Column('longitude', sa.Float(), nullable=False),
    sa.PrimaryKeyConstraint('id'),
    sa.UniqueConstraint('id')
    )
    op.create_table('users',
    sa.Column('id', sa.Integer(), autoincrement=True, nullable=False),
    sa.Column('id_bank_data', sa.Integer(), nullable=True),
    sa.Column('national_id_document', sa.String(), nullable=True),
    sa.Column('country', sa.String(), nullable=True),
    sa.Column('name', sa.String(), nullable=True),
    sa.Column('surname', sa.String(), nullable=True),
    sa.Column('mail', sa.String(), nullable=False),
    sa.Column('google_token', sa.String(), nullable=False),
    sa.Column('role', sa.Integer(), nullable=False),
    sa.PrimaryKeyConstraint('id'),
    sa.UniqueConstraint('google_token'),
    sa.UniqueConstraint('id'),
    sa.UniqueConstraint('id_bank_data'),
    sa.UniqueConstraint('mail')
    )
    op.create_table('rentals',
    sa.Column('id', sa.Integer(), autoincrement=True, nullable=False),
    sa.Column('moto_id', sa.Integer(), nullable=False),
    sa.Column('user_id', sa.Integer(), nullable=False),
    sa.Column('active', sa.Boolean(), nullable=False),
    sa.Column('book_hour', sa.String(), nullable=False),
    sa.Column('finish_book_hour', sa.String(), nullable=False),
    sa.Column('finish_rental_hour', sa.String(), nullable=True),
    sa.ForeignKeyConstraint(['moto_id'], ['motos.id'], ),
    sa.ForeignKeyConstraint(['user_id'], ['users.id'], ),
    sa.PrimaryKeyConstraint('id'),
    sa.UniqueConstraint('id')
    )
    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.drop_table('rentals')
    op.drop_table('users')
    op.drop_table('motos')
    op.drop_table('map_coords')
    # ### end Alembic commands ###